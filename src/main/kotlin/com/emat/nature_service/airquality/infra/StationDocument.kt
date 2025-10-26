package com.emat.nature_service.airquality.infra

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "stations")
data class StationDocument(
    @Id
    val id: String? = null,

    @Indexed(unique = true)
    val stationId: String,

    val code: String,
    val name: String,
    val latitude: String,
    val longitude: String,
    val cityId: String?,
    val city: String?,
    val commune: String?,
    val district: String?,
    val voivodeship: String?,
    val street: String?
) {
}