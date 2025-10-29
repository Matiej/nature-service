package com.emat.nature_service.client.n8n

data class EmailNotificationRequest(
    val stationsFetched: Int,
    val stationsSaved: Int,
    val stationsUpdated:Int,
    val measurements:Int,
    val errorMessage: String
) {
}