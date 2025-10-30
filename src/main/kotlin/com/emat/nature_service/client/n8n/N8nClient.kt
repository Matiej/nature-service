package com.emat.nature_service.client.n8n

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class N8nClient(
    private val n8nWebClient: WebClient
) {
    companion object {
        private const val EMAIL_AIR_QUALITY_JOB_URI = "/gios/email"
        private const val EMAIL_WEATHER_JOB_URI = "/imgw/email"
        private val log = LoggerFactory.getLogger(N8nClient::class.java)
    }

    fun sendEmailNotification(request: EmailNotificationRequest): Mono<Void> =
        n8nWebClient
            .post()
            .uri(EMAIL_AIR_QUALITY_JOB_URI)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void::class.java)
            .doOnSubscribe { log.info("Calling n8n email notifier: {}", EMAIL_AIR_QUALITY_JOB_URI) }
            .doOnSuccess { log.info("n8n email notifier responded with 2xx (no body).") }
            .doOnError { e -> log.error("Error calling n8n email notifier", e) }

    fun sendWeatherEmailNotification(request: WeatherEmailNotificationRequest): Mono<Void> =
        n8nWebClient
            .post()
            .uri(EMAIL_WEATHER_JOB_URI)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void::class.java)
            .doOnSubscribe { log.info("Calling n8n email notifier: {}", EMAIL_WEATHER_JOB_URI) }
            .doOnSuccess { log.info("n8n email notifier responded with 2xx (no body).") }
            .doOnError { e -> log.error("Error calling n8n email notifier", e) }
}