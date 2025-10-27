package com.emat.nature_service.airquality

import com.emat.nature_service.airquality.domain.GiosAqIndex
import com.emat.nature_service.airquality.domain.GiosStations
import com.emat.nature_service.api.TakingMeasurementsResponse
import reactor.core.publisher.Mono

interface AirQualityService {
    fun synchronizeStations(): Mono<Triple<GiosStations, GiosStations, GiosStations>>
    fun getAqIndex(stationId: String): Mono<GiosAqIndex>

    fun saveMeasurementsForAllStationsSlow(): Mono<TakingMeasurementsResponse>
}