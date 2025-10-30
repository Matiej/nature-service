package com.emat.nature_service.client.imgw

import com.emat.nature_service.client.imgw.resposne.ImgwSynopResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ImgwClient(
    private val imgwWebClient: WebClient
) {

    companion object {
        private const val IMGW_SYNOP_ENDPOINT = "/api/data/synop"
        private val log = LoggerFactory.getLogger(ImgwClient::class.java)
    }

    fun fetchAllSynop(): Flux<ImgwSynopResponse> =
        imgwWebClient
            .get()
            .uri(IMGW_SYNOP_ENDPOINT)
            .retrieve()
            .bodyToFlux(ImgwSynopResponse::class.java)
            .doOnSubscribe { log.info("Calling IMGW: {}", IMGW_SYNOP_ENDPOINT) }
            .doOnError { e -> log.error("Error calling IMGW API", e) }

    fun fetchAllSynopAsList(): Mono<List<ImgwSynopResponse>> =
        fetchAllSynop()
            .collectList()
            .doOnSuccess { list ->
                log.info("Fetched {} SYNOP records from IMGW", list.size)
            }
}