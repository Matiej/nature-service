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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StationDocument

        if (stationId != other.stationId) return false
        if (code != other.code) return false
        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (cityId != other.cityId) return false
        if (city != other.city) return false
        if (commune != other.commune) return false
        if (district != other.district) return false
        if (voivodeship != other.voivodeship) return false
        if (street != other.street) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stationId.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + (cityId?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (commune?.hashCode() ?: 0)
        result = 31 * result + (district?.hashCode() ?: 0)
        result = 31 * result + (voivodeship?.hashCode() ?: 0)
        result = 31 * result + (street?.hashCode() ?: 0)
        return result
    }
}