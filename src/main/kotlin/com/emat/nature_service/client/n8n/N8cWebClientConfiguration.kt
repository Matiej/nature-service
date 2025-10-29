package com.emat.nature_service.client.n8n

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class N8cWebClientConfiguration(
    @Value("\${n8n.base-url}")
    private val n8nApiBaseUrl: String
) {

    @Bean
    fun n8nWebClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl(n8nApiBaseUrl)
            .defaultHeaders { it.addAll(defaultHeaders()) }
            .build()

    private fun defaultHeaders(): HttpHeaders = HttpHeaders().apply {
        add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
    }

}