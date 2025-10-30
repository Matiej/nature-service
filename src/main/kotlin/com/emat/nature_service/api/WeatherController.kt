package com.emat.nature_service.api

import com.emat.nature_service.weather.WeatherService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/weather")
class WeatherController(
    private val weatherService: WeatherService
) {

    private val log = LoggerFactory.getLogger(WeatherController::class.java)

    @Operation(
        summary = "Fetch and save IMGW measurements",
        description = "Fetches and stores a list of IMGW weather measurements."
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Stations successfully synchronized",
        ), ApiResponse(responseCode = "500", description = "Internal server error")]
    )
    @GetMapping("/measurements/fetch")
    @ResponseStatus(HttpStatus.OK)
    fun takeAllStationsMeasurements(
    ): Mono<WeatherTakingMeasurementsResponse> {
        log.info("Incoming request on /air-quality/measurements/fetch")
        return weatherService.fetchAndStoreCurrentMeasurements()
    }
}