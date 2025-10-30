package com.emat.nature_service.api

import com.emat.nature_service.airquality.AirQualityService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/air-quality")
class AirQualityController(
    private val airQualityService: AirQualityService
) {
    private val log = LoggerFactory.getLogger(AirQualityController::class.java)

    @Operation(
        summary = "Synchronize GIOS monitoring stations",
        description = "Fetches and stores a list of GIOS air quality monitoring stations."
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Stations successfully synchronized",
        ), ApiResponse(responseCode = "500", description = "Internal server error")]
    )
    @GetMapping("/measurements/fetch")
    fun takeAllStationsMeasurements(
        @RequestParam(defaultValue = "500") duration: Int
    ): Mono<TakingMeasurementsResponse> {
        log.info("Incoming request on /air-quality/measurements/fetch")
        return airQualityService.saveMeasurementsForAllStationsSlow(duration)
    }
}