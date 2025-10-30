package com.emat.nature_service.weather

import com.emat.nature_service.api.WeatherTakingMeasurementsResponse
import reactor.core.publisher.Mono

interface WeatherService {
    fun fetchAndStoreCurrentMeasurements(): Mono<WeatherTakingMeasurementsResponse>
}