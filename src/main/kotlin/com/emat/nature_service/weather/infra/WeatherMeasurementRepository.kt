package com.emat.nature_service.weather.infra

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface WeatherMeasurementRepository: ReactiveMongoRepository<WeatherMeasurementDocument, String> {
    fun findFirstByStationIdOrderByMeasurementDateDesc(stationId: String): Mono<WeatherMeasurementDocument>

    fun findByStationIdAndMeasurementDate(stationId: String, measurementDate: String): Flux<WeatherMeasurementDocument>
}