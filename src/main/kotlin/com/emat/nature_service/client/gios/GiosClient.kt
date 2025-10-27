package com.emat.nature_service.client.gios

import com.emat.nature_service.client.gios.response.GiosAqIndexResponse
import com.emat.nature_service.client.gios.response.GiosStationsResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class GiosClient(
    private val giosWebClient: WebClient
) {
    companion object {
        private const val GET_ALL_STATIONS_URI = "/v1/rest/station/findAll"
        private const val AQ_INDEX_URI = "/v1/rest/aqindex/getIndex/{stationId}"
        private const val MAX_STATIONS_SIZE = "500"
        private val log = LoggerFactory.getLogger(GiosClient::class.java)
    }

    fun getAllStations(): Mono<GiosStationsResponse> =
        giosWebClient
            .get()
            .uri { builder ->
                builder.path(GET_ALL_STATIONS_URI)
                    .queryParam("size", MAX_STATIONS_SIZE)
                    .build()
            }
            .retrieve()
            .bodyToMono(GiosStationsResponse::class.java)
            .doOnSubscribe { log.info("Calling GIOS: {}", GET_ALL_STATIONS_URI) }
            .doOnError { e -> log.error("Error calling GIOS API", e) }

    fun getAqIndex(stationId: String): Mono<GiosAqIndexResponse> =
        giosWebClient
            .get()
            .uri { builder ->
                builder.path(AQ_INDEX_URI)
                    .build(stationId)
            }
            .retrieve()
            .bodyToMono(GiosAqIndexResponse::class.java)
            .doOnSubscribe { log.info("Calling GIOS aqindex: {}", AQ_INDEX_URI) }
            .doOnError { e -> log.error("Error calling GIOS API", e) }
}