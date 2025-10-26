package com.emat.nature_service.airquality.infra

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface StationRepository : ReactiveMongoRepository<StationDocument, String> {
    fun findByStationId(stationId: String): Mono<StationDocument>
}
