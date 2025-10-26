package com.emat.nature_service.client.gios

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GiosWebClientConfiguration(
    @Value("\${gios.base-url}")
    private val giosApiBaseUrl: String
) {
    @Bean
    fun giosWebClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl(giosApiBaseUrl)
            .defaultHeaders { it.addAll(defaultHeaders()) }
            .build()

    private fun defaultHeaders(): HttpHeaders = HttpHeaders().apply {
        add(HttpHeaders.ACCEPT, "application/ld+json")
    }
}