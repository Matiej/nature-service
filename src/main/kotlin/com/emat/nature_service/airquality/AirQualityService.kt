package com.emat.nature_service.airquality

import com.emat.nature_service.airquality.domain.GiosAqIndex
import com.emat.nature_service.airquality.domain.GiosStations
import com.emat.nature_service.airquality.infra.AqIndexDocument
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AirQualityService {
    fun synchronizeStations(): Mono<GiosStations>
    fun getAqIndex(stationId: String): Mono<GiosAqIndex>
    fun saveMeasurementsForAllStations(): Flux<AqIndexDocument>

}