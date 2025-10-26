package com.emat.nature_service.client.gios.response

import com.emat.nature_service.airquality.domain.GiosAqIndex
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GiosAqIndexResponse(
    @JsonProperty("AqIndex")
    val aqIndex: AqIndexResponse? = null
) {
    fun toDomain(): GiosAqIndex? = aqIndex?.toDomain()

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AqIndexResponse(
        @JsonProperty("Identyfikator stacji pomiarowej") val stationId: String,
        @JsonProperty("Data wykonania obliczeń indeksu") val calculationDate: String?,
        @JsonProperty("Wartość indeksu") val indexValue: Int,
        @JsonProperty("Nazwa kategorii indeksu") val indexCategory: String?,
        @JsonProperty("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika st") val sourceDataDate: String?,
        @JsonProperty("Data wykonania obliczeń indeksu dla wskaźnika SO2") val so2CalculationDate: String?,
        @JsonProperty("Wartość indeksu dla wskaźnika SO2") val so2IndexValue: Int?,
        @JsonProperty("Nazwa kategorii indeksu dla wskażnika SO2") val so2IndexCategory: String?,
        @JsonProperty("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika SO2") val so2SourceDataDate: String?,
        @JsonProperty("Data wykonania obliczeń indeksu dla wskaźnika NO2") val no2CalculationDate: String?,
        @JsonProperty("Wartość indeksu dla wskaźnika NO2") val no2IndexValue: Int?,
        @JsonProperty("Nazwa kategorii indeksu dla wskażnika NO2") val no2IndexCategory: String?,
        @JsonProperty("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika NO2") val no2SourceDataDate: String?,
        @JsonProperty("Data wykonania obliczeń indeksu dla wskaźnika PM10") val pm10CalculationDate: String?,
        @JsonProperty("Wartość indeksu dla wskaźnika PM10") val pm10IndexValue: Int?,
        @JsonProperty("Nazwa kategorii indeksu dla wskażnika PM10") val pm10IndexCategory: String?,
        @JsonProperty("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika PM10") val pm10SourceDataDate: String?,
        @JsonProperty("Data wykonania obliczeń indeksu dla wskaźnika PM2.5") val pm25CalculationDate: String?,
        @JsonProperty("Wartość indeksu dla wskaźnika PM2.5") val pm25IndexValue: Int?,
        @JsonProperty("Nazwa kategorii indeksu dla wskażnika PM2.5") val pm25IndexCategory: String?,
        @JsonProperty("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika PM2.5") val pm25SourceDataDate: String?,
        @JsonProperty("Data wykonania obliczeń indeksu dla wskaźnika O3") val o3CalculationDate: String?,
        @JsonProperty("Wartość indeksu dla wskaźnika O3") val o3IndexValue: Int?,
        @JsonProperty("Nazwa kategorii indeksu dla wskażnika O3") val o3IndexCategory: String?,
        @JsonProperty("Data danych źródłowych, z których policzono wartość indeksu dla wskaźnika O3") val o3SourceDataDate: String?,
        @JsonProperty("Status indeksu ogólnego dla stacji pomiarowej") val stationIndexStatus: Boolean,
        @JsonProperty("Kod zanieczyszczenia krytycznego") val criticalPollutantCode: String?
    ) {
        fun toDomain(): GiosAqIndex = GiosAqIndex(
            stationId = stationId,
            calculationDate = calculationDate,
            indexValue = indexValue,
            indexCategory = indexCategory,
            sourceDataDate = sourceDataDate,
            so2CalculationDate = so2CalculationDate,
            so2IndexValue = so2IndexValue,
            so2IndexCategory = so2IndexCategory,
            so2SourceDataDate = so2SourceDataDate,
            no2CalculationDate = no2CalculationDate,
            no2IndexValue = no2IndexValue,
            no2IndexCategory = no2IndexCategory,
            no2SourceDataDate = no2SourceDataDate,
            pm10CalculationDate = pm10CalculationDate,
            pm10IndexValue = pm10IndexValue,
            pm10IndexCategory = pm10IndexCategory,
            pm10SourceDataDate = pm10SourceDataDate,
            pm25CalculationDate = pm25CalculationDate,
            pm25IndexValue = pm25IndexValue,
            pm25IndexCategory = pm25IndexCategory,
            pm25SourceDataDate = pm25SourceDataDate,
            o3CalculationDate = o3CalculationDate,
            o3IndexValue = o3IndexValue,
            o3IndexCategory = o3IndexCategory,
            o3SourceDataDate = o3SourceDataDate,
            stationIndexStatus = stationIndexStatus,
            criticalPollutantCode = criticalPollutantCode
        )
    }
}
