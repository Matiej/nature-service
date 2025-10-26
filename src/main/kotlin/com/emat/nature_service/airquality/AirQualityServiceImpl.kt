package com.emat.nature_service.airquality

import com.emat.nature_service.airquality.domain.GiosAqIndex
import com.emat.nature_service.airquality.domain.GiosStation
import com.emat.nature_service.airquality.domain.GiosStations
import com.emat.nature_service.airquality.infra.AqIndexDocument
import com.emat.nature_service.airquality.infra.AqIndexRepository
import com.emat.nature_service.airquality.infra.StationDocument
import com.emat.nature_service.airquality.infra.StationRepository
import com.emat.nature_service.client.gios.GiosClient
import com.emat.nature_service.client.gios.response.GiosStationsResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.*
import kotlin.random.Random


@Service
class AirQualityServiceImpl(
    private val giosClient: GiosClient,
    private val stationRepository: StationRepository,
    private val aqIndexRepository: AqIndexRepository
) : AirQualityService {
    private val log = LoggerFactory.getLogger(AirQualityServiceImpl::class.java)
    private val AQ_CONCURRENCY = 3

    override fun synchronizeStations(): Mono<GiosStations> =
        giosClient.getAllStations()
            .doOnNext { r -> log.info("Received {} stations from GIOS", r.getNumberOfStations()) }
            .map(GiosStationsResponse::toDomain)
            .flatMap { stations ->
                updateExistingStations(stations.stationList)
                    .then(saveNewStations(stations.stationList))
                    .thenReturn(stations)
            }

    override fun getAqIndex(stationId: String): Mono<GiosAqIndex> =
        giosClient.getAqIndex(stationId)
            .flatMap { response ->
                val domain = response.toDomain()
                if (domain != null) Mono.just(domain) else Mono.empty()
            }
            .filter { !it.stationId.isNullOrBlank() }
            .doOnNext { log.info("Received aqIndex for station {}", stationId) }

    override fun saveMeasurementsForAllStations(): Flux<AqIndexDocument> =
        synchronizeStations()
            .map(GiosStations::stationList)
            .flatMapMany { Flux.fromIterable(it) }
            .flatMap({ station ->
                // mały jitter aby rozproszyć żądania
                val jitter = Random.nextLong(100, 350)
                Mono.delay(Duration.ofMillis(jitter))
                    .then(getAqIndex(station.stationId))
                    .filter { !it.stationId.isNullOrBlank() }      // pomijaj puste stationId
                    .map { it.toDocument() }
                    .flatMap { aqIndexRepository.save(it) }
                    .doOnNext { saved -> log.info("Saved AQ index for stationId={}", saved.stationId) }
            }, AQ_CONCURRENCY)
            .subscribeOn(Schedulers.boundedElastic())

    private fun updateExistingStations(stationList: List<GiosStation>): Mono<Void> =
        Flux.fromIterable(stationList)
            .flatMap { station ->
                val newDoc = station.toDocument()
                stationRepository.findByStationId(newDoc.stationId)
                    .flatMap { existing ->
                        // porównujemy wszystkie pola poza _id (data class porówna też null-e)
                        if (existing.copy(id = null) == newDoc.copy(id = null)) {
                            Mono.empty() // identyczne → pomijamy
                        } else {
                            // różne → aktualizujemy (zachowujemy _id)
                            stationRepository.save(newDoc.copy(id = existing.id))
                                .doOnNext { saved -> log.info("Updated station id: {}", saved.stationId) }
                                .then()
                        }
                    }
                    .switchIfEmpty(Mono.empty()) // brak w bazie → doda saveNewStations()
            }
            .then()

    private fun saveNewStations(stationList: List<GiosStation>): Mono<Void> =
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
                                .onErrorResume(org.springframework.dao.DuplicateKeyException::class.java) {
                                    // w razie rzadkiego wyścigu przy równoległych zapisach
                                    log.debug("Duplicate on stationId={}, ignoring insert.", newDoc.stationId)
                                    Mono.empty()
                                }
                                .then()
                        }
                    }
            }
            .then()
}