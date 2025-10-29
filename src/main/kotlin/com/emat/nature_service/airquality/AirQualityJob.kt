package com.emat.nature_service.airquality

import com.emat.nature_service.client.n8n.N8nService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AirQualityJob(
    private val airQualityService: AirQualityService,
    private val nnN8nService: N8nService,
    @Value("\${scheduler.airquality.cron}")
    private val cronExpression: String
) {
    private val log = LoggerFactory.getLogger(AirQualityJob::class.java)

    @Scheduled(cron = "\${scheduler.airquality.cron}")
    fun runAirQualitySyncJob() {
        airQualityService.saveMeasurementsForAllStationsSlow()
            .doOnSubscribe { log.info("Running AirQualityJob with cron: {}", cronExpression) }
            .flatMap { measurementResult ->
                nnN8nService.sendMeasurementEmailNotification(measurementResult, null)
                    .onErrorResume { emailError ->
                        log.warn("Failed to send SUCCESS email to n8n: {}", emailError.message)
                        Mono.empty()
                    }
                    .thenReturn(measurementResult)
            }
            .doOnSuccess { suc ->
                log.info("✅ Air quality synchronization finished successfully. Fetched: ${suc.savedMeasurements} measurements" )
            }
            .onErrorResume { error ->
                nnN8nService.sendMeasurementEmailNotification(null, error.message)
                    .onErrorResume { emailError ->
                        nnN8nService.sendMeasurementEmailNotification(null, emailError.message)
                        Mono.empty()
                    }.then(
                        Mono.fromRunnable { log.error("❌ Air quality synchronization failed", error) }
                    )
                 }
            .subscribe()
    }
}