package com.emat.nature_service.weather

import com.emat.nature_service.api.WeatherTakingMeasurementsResponse
import com.emat.nature_service.client.imgw.ImgwClient
import com.emat.nature_service.client.imgw.resposne.ImgwSynopResponse
import com.emat.nature_service.weather.infra.WeatherMeasurementDocument
import com.emat.nature_service.weather.infra.WeatherMeasurementRepository

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Instant

@Service
class WeatherServiceImpl(
    private val imgwClient: ImgwClient,
    private val weatherMeasurementRepository: WeatherMeasurementRepository
) : WeatherService {

    private val log = LoggerFactory.getLogger(WeatherServiceImpl::class.java)

    override fun fetchAndStoreCurrentMeasurements(): Mono<WeatherTakingMeasurementsResponse> {
        val fetchedAt = Instant.now()
        return imgwClient.fetchAllSynopAsList()
            .flatMap { synopList ->
                val totalFetched = synopList.size
                Flux.fromIterable(synopList)
                    .flatMap { dto ->
                        val weatherMeasurementDocument = dto.toDocument(fetchedAt)
                        weatherMeasurementRepository.save(weatherMeasurementDocument)
                            .thenReturn(true)
                            .onErrorResume(DuplicateKeyException::class.java) {
                                log.debug(
                                    "Duplicate weather measurement for stationId={}, date={}, hour={}",
                                    weatherMeasurementDocument.stationId,
                                    weatherMeasurementDocument.measurementDate,
                                    weatherMeasurementDocument.measurementHour
                                )
                                Mono.just(false)
                            }
                    }.collectList()
                    .map { result ->
                        val saved = result.count { it }
                        val skipped = totalFetched - saved
                        WeatherTakingMeasurementsResponse(
                            fetchedMeasurements = totalFetched,
                            savedMeasurements = saved,
                            skippedMeasurements = skipped
                        )
                    }.doOnSuccess { suc ->
                        log.info(
                            "IMGW measurements saved. fetched={}, saved={}, skipped={}",
                            suc.fetchedMeasurements,
                            suc.savedMeasurements,
                            suc.skippedMeasurements
                        )
                    }.doOnError { e -> log.error("Error while saving IMGW measurements", e) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
    }


    fun ImgwSynopResponse.toDocument(fetchedAt: Instant) =
        WeatherMeasurementDocument(
            stationId = this.stationId,
            stationName = this.stationName,
            measurementDate = this.measurementDate,
            measurementHour = this.measurementHour,
            temperature = this.temperature,
            windSpeed = this.windSpeed,
            windDirection = this.windDirection,
            relativeHumidity = this.relativeHumidity,
            precipitation = this.precipitation,
            pressure = this.pressure,
            createdAt = fetchedAt
        )
}