package com.emat.nature_service.airquality.infra

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "aqindex")
data class AqIndexDocument(
    @Id
    val id: String? = null,
    val savingDate: String,
    val stationId: String?,
    val calculationDate: String?,
    val indexValue: Int?,
    val indexCategory: String?,
    val sourceDataDate: String?,
    val so2CalculationDate: String?,
    val so2IndexValue: Int?,
    val so2IndexCategory: String?,
    val so2SourceDataDate: String?,
    val no2CalculationDate: String?,
    val no2IndexValue: Int?,
    val no2IndexCategory: String?,
    val no2SourceDataDate: String?,
    val pm10CalculationDate: String?,
    val pm10IndexValue: Int?,
    val pm10IndexCategory: String?,
    val pm10SourceDataDate: String?,
    val pm25CalculationDate: String?,
    val pm25IndexValue: Int?,
    val pm25IndexCategory: String?,
    val pm25SourceDataDate: String?,
    val o3CalculationDate: String?,
    val o3IndexValue: Int?,
    val o3IndexCategory: String?,
    val o3SourceDataDate: String?,
    val stationIndexStatus: Boolean,
    val criticalPollutantCode: String?
) {

}