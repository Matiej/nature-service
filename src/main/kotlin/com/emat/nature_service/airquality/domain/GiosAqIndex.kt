package com.emat.nature_service.airquality.domain

import com.emat.nature_service.airquality.infra.AqIndexDocument
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GiosAqIndex(
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

    fun toDocument(): AqIndexDocument = AqIndexDocument(
        stationId = stationId,
        savingDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
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
