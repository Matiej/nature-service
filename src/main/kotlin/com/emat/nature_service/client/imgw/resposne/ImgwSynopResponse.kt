package com.emat.nature_service.client.imgw.resposne

import com.fasterxml.jackson.annotation.JsonProperty

data class ImgwSynopResponse(
    @JsonProperty("id_stacji")
    val stationId: String?,

    @JsonProperty("stacja")
    val stationName: String?,

    @JsonProperty("data_pomiaru")
    val measurementDate: String?,   // format: yyyy-MM-dd

    @JsonProperty("godzina_pomiaru")
    val measurementHour: String?,   // e.g. "06", "12", "18"

    @JsonProperty("temperatura")
    val temperature: String?,

    @JsonProperty("predkosc_wiatru")
    val windSpeed: String?,

    @JsonProperty("kierunek_wiatru")
    val windDirection: String?,

    @JsonProperty("wilgotnosc_wzgledna")
    val relativeHumidity: String?,

    @JsonProperty("suma_opadu")
    val precipitation: String?,

    @JsonProperty("cisnienie")
    val pressure: String?
)
