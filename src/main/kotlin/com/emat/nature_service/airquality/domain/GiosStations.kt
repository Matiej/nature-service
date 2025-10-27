package com.emat.nature_service.airquality.domain

import com.emat.nature_service.airquality.infra.StationDocument

data class GiosStations(
    val stationList: List<GiosStation>,
    val numberOfStations: Int
) {
}

data class GiosStation(
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
    fun toDocument(): StationDocument =
        StationDocument(
            stationId = stationId,
            code = code,
            name = name,
            latitude = latitude,
            longitude = longitude,
            cityId = cityId,
            city = city,
            commune = commune,
            district = district,
            voivodeship = voivodeship,
            street = street
        )
}

fun StationDocument.toDomain(): GiosStation =
    GiosStation(
        stationId = stationId,
        code = code,
        name = name,
        latitude = latitude,
        longitude = longitude,
        cityId = cityId,
        city = city,
        commune = commune,
        district = district,
        voivodeship = voivodeship,
        street = street
    )