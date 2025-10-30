package com.emat.nature_service.weather

import com.emat.nature_service.airquality.AirQualityJob
import com.emat.nature_service.client.n8n.N8nService
import org.slf4j.LoggerFactory

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class WeatherJob(
    private val n8nService: N8nService,
    private val weatherService: WeatherService,

    ) {
    private val log = LoggerFactory.getLogger(WeatherJob::class.java)

    @Scheduled(cron = "\${scheduler.weather.cron}")
    fun runWeatherSyncJob() {
        weatherService.fetchAndStoreCurrentMeasurements()
            .doOnSubscribe { log.info("🌦 Running WeatherJob with cron") }
            .flatMap { measurementResult ->
                n8nService.sendWeatherEmailNotification(measurementResult, null)
                    .onErrorResume { emailError ->
                        log.warn("⚠️ Failed to send SUCCESS weather email to n8n: {}", emailError.message)
                        Mono.empty()
                    }
                    .thenReturn(measurementResult)
            }
            .doOnSuccess { result ->
                log.info(
                    "✅ Weather synchronization finished successfully. " +
                            "Fetched: ${result.fetchedMeasurements}, Saved: ${result.savedMeasurements}, Skipped: ${result.skippedMeasurements}"
                )
            }
            .onErrorResume { error ->
                n8nService.sendWeatherEmailNotification(null, error.message)
                    .onErrorResume { emailError ->
                        log.warn("⚠️ Failed to send ERROR weather email: {}", emailError.message)
                        Mono.empty()
                    }
                    .then(
                        Mono.fromRunnable {
                            log.error("❌ Weather synchronization failed", error)
                        }
                    )
            }
            .subscribe()
    }

}