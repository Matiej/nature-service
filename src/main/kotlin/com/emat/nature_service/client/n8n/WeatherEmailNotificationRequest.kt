package com.emat.nature_service.client.n8n

data class WeatherEmailNotificationRequest(
    val fetchedMeasurements: Int,
    val savedMeasurements: Int,
    val skippedMeasurements: Int,
    val errorMessage: String?
) {
}