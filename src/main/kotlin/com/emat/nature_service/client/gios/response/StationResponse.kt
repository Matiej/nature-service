package com.emat.nature_service.client.gios.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StationResponse(
    @JsonProperty("Identyfikator stacji") val id: String,
    @JsonProperty("Kod stacji") val code: String,
    @JsonProperty("Nazwa stacji") val name: String,
    @JsonProperty("WGS84 φ N") val latitude: String,
    @JsonProperty("WGS84 λ E") val longitude: String,
    @JsonProperty("Identyfikator miasta") val cityId: String?,
    @JsonProperty("Nazwa miasta") val city: String?,
    @JsonProperty("Gmina") val commune: String?,
    @JsonProperty("Powiat") val district: String?,
    @JsonProperty("Województwo") val voivodeship: String?,
    @JsonProperty("Ulica") val street: String?
)
