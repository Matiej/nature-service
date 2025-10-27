package com.emat.nature_service.airquality

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled

//@Component
class AirQualityJob(
    private val airQualityService: AirQualityService,
    @Value("\${scheduler.airquality.cron}")
    private val cronExpression: String
) {
    private val log = LoggerFactory.getLogger(AirQualityJob::class.java)

    @Scheduled(cron = "\${scheduler.airquality.cron}")
    fun runAirQualitySyncJob() {
        airQualityService.saveMeasurementsForAllStationsSlow()
            .then() // Mono<Void> – koniec pracy
            .doOnSubscribe { log.info("Running AirQualityJob with cron: {}", cronExpression) }
            .doOnSuccess { log.info("✅ Air quality synchronization finished successfully.") }
            .doOnError { e -> log.error("❌ Air quality synchronization failed", e) }
            .subscribe()
    }
}