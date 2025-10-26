package com.emat.nature_service.client.gios.response

import com.emat.nature_service.airquality.domain.GiosStation
import com.emat.nature_service.airquality.domain.GiosStations
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GiosStationsResponse(
    @JsonProperty("Lista stacji pomiarowych")
    val stationsResponse: List<StationResponse>? = null
) {
    fun getNumberOfStations(): Int = stationsResponse?.size ?: 0

    fun toDomain(): GiosStations {
        val stations = stationsResponse.orEmpty().map { station ->
            GiosStation(
                station.id,
                station.code,
                station.name,
                station.latitude,
                station.longitude,
                station.cityId,
                station.city,
                station.commune,
                station.district,
                station.voivodeship,
                station.street
            )
        }
        return GiosStations(stations, getNumberOfStations())
    }

}
