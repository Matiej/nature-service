package com.emat.nature_service.client.imgw

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ImgwWebClientConfiguration(
    @Value("\${imgw.base-url}")
    private val imgwBaseUrl: String,
) {

    @Bean
    fun imgwWebClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl(imgwBaseUrl)
            .defaultHeaders { headers ->
                headers.add(HttpHeaders.ACCEPT, "application/json")
            }
            .build()
}