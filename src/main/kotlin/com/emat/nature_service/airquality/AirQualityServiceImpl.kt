package com.emat.nature_service.airquality

import com.emat.nature_service.airquality.domain.GiosAqIndex
import com.emat.nature_service.airquality.domain.GiosStation
import com.emat.nature_service.airquality.domain.GiosStations
import com.emat.nature_service.airquality.domain.toDomain
import com.emat.nature_service.airquality.infra.AqIndexRepository
import com.emat.nature_service.airquality.infra.StationRepository
import com.emat.nature_service.api.TakingMeasurementsResponse
import com.emat.nature_service.client.gios.GiosClient
import com.emat.nature_service.client.gios.response.GiosStationsResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration


@Service
class AirQualityServiceImpl(
    private val giosClient: GiosClient,
    private val stationRepository: StationRepository,
    private val aqIndexRepository: AqIndexRepository
) : AirQualityService {
    private val log = LoggerFactory.getLogger(AirQualityServiceImpl::class.java)

    override fun synchronizeStations(): Mono<Triple<GiosStations, GiosStations, GiosStations>> =
        giosClient.getAllStations()
            .doOnNext { r -> log.info("Received {} stations from GIOS", r.getNumberOfStations()) }
            .map(GiosStationsResponse::toDomain)
            .flatMap { stations ->
                val updateExistingStations = updateExistingStations(stations.stationList)
                val saveNewStations = saveNewStations(stations.stationList)

                Mono.zip(updateExistingStations, saveNewStations) { updated, saved ->
                    Triple(stations, updated, saved)
                }
            }

    override fun getAqIndex(stationId: String): Mono<GiosAqIndex> =
        giosClient.getAqIndex(stationId)
            .flatMap { response ->
                val domain = response.toDomain()
                if (domain != null) Mono.just(domain) else Mono.empty()
            }
            .filter { !it.stationId.isNullOrBlank() }
            .doOnNext { log.info("Received aqIndex for station {}", stationId) }

    override fun saveMeasurementsForAllStationsSlow(duration: Int): Mono<TakingMeasurementsResponse> =
        synchronizeStations()
            .flatMap { par ->
                Flux.fromIterable(par.first.stationList)
                    .delayElements(Duration.ofMillis(duration.toLong()))
                    .concatMap { station ->
                        getAqIndexRespecting429(station.stationId!!)
                            .filter { !it.stationId.isNullOrBlank() }
                            .map { it.toDocument() }
                            .flatMap(aqIndexRepository::save)
                            .doOnNext { saved ->
                                log.info("Saved AQ index (SLOW) for stationId={}", saved.stationId)
                            }
                    }
                    .collectList()
                    .map { savedList ->
                        TakingMeasurementsResponse(
                            fetchedStations = par.first,
                            updatedStations = par.second,
                            savedStations = par.third,
                            savedMeasurements = savedList.size
                        )
                    }
            }.subscribeOn(Schedulers.boundedElastic())


    /** Helper: woła GIOŚ i szanuje 429 + Retry-After (jedna powtórka). */
    private fun getAqIndexRespecting429(stationId: String): Mono<GiosAqIndex> =
        giosClient.getAqIndex(stationId)
            .onErrorResume(WebClientResponseException.TooManyRequests::class.java) { ex ->
                log.warn("GIOŚ 429 status for station {}. Retrying once after 5s", stationId)
                Mono.delay(Duration.ofSeconds(5))
                    .then(giosClient.getAqIndex(stationId))
            }
            .flatMap { resp -> resp.toDomain()?.let { Mono.just(it) } ?: Mono.empty() }
            .filter { !it.stationId.isNullOrBlank() }

    private fun updateExistingStations(stationList: List<GiosStation>): Mono<GiosStations> =
        Flux.fromIterable(stationList)
            .flatMap { station ->
                val newStationDocument = station.toDocument()
                stationRepository.findByStationId(newStationDocument.stationId)
                    .flatMap { existing ->
                        if (existing == newStationDocument) {
                            Mono.empty()
                        } else {
                            stationRepository.save(newStationDocument.copy(id = existing.id))
                                .doOnNext { saved -> log.info("Updated station id: {}", saved.stationId) }

                        }
                    }
                    .map { it.toDomain() }
                    .switchIfEmpty(Mono.empty())
            }.collectList()
            .map { updatedList -> GiosStations(updatedList, updatedList.size) }

    private fun saveNewStations(stationList: List<GiosStation>): Mono<GiosStations> =
        Flux.fromIterable(stationList)
            .flatMap { station ->
                val newDoc = station.toDocument()
                stationRepository.findByStationId(newDoc.stationId)
                    .hasElement()
                    .flatMap { exists ->
                        if (exists) {
                            Mono.empty() // już jest → nic nie robimy
                        } else {
                            stationRepository.save(newDoc)
                                .doOnNext { saved -> log.info("Inserted station id: {}", saved.stationId) }
                        }
                    }

            }.map { it.toDomain() }
            .collectList()
            .map { updatedList -> GiosStations(updatedList, updatedList.size) }


}