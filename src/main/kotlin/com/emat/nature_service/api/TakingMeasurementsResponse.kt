package com.emat.nature_service.api

import com.emat.nature_service.airquality.domain.GiosStations

data class TakingMeasurementsResponse(
    val fetchedStations: GiosStations?,
    val updatedStations: GiosStations?,
    val savedStations: GiosStations?,
    val savedMeasurements: Int
)
