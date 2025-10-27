package com.emat.nature_service.airquality.infra

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface AqIndexRepository : ReactiveMongoRepository<AqIndexDocument, String> {
    fun findByStationId(stationId: String): Flux<AqIndexDocument>
    fun findFirstByStationIdOrderBySavingDateDesc(stationId: String): Mono<AqIndexDocument>
}
