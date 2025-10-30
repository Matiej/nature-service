package com.emat.nature_service.api

data class WeatherTakingMeasurementsResponse(
    val fetchedMeasurements: Int,
    val savedMeasurements: Int,
    val skippedMeasurements: Int
)
