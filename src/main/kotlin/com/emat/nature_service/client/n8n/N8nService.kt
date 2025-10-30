package com.emat.nature_service.client.n8n

import com.emat.nature_service.api.TakingMeasurementsResponse
import com.emat.nature_service.api.WeatherTakingMeasurementsResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class N8nService(
    private val n8nClient: N8nClient
) {
    fun sendMeasurementEmailNotification(
        measurementsResponse: TakingMeasurementsResponse?,
        errorMessage: String?
    ): Mono<Void> =
        n8nClient.sendEmailNotification(
            EmailNotificationRequest(
                stationsFetched = measurementsResponse?.fetchedStations?.numberOfStations ?: 0,
                stationsUpdated = measurementsResponse?.updatedStations?.numberOfStations ?: 0,
                stationsSaved = measurementsResponse?.savedStations?.numberOfStations ?: 0,
                measurements = measurementsResponse?.savedMeasurements ?: 0,
                errorMessage = errorMessage ?: "No errors"
            )
        )

    fun sendWeatherEmailNotification(
        measurementResult: WeatherTakingMeasurementsResponse?,
        errorMessage: String?
    ): Mono<Void> =
        n8nClient.sendWeatherEmailNotification(
            WeatherEmailNotificationRequest(
                fetchedMeasurements = measurementResult?.fetchedMeasurements ?: 0,
                savedMeasurements = measurementResult?.savedMeasurements ?: 0,
                skippedMeasurements = measurementResult?.skippedMeasurements ?: 0,
                errorMessage = errorMessage
            )
        )
}