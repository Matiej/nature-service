package com.emat.nature_service.weather.infra

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "weather_measurements")
@CompoundIndexes(
    CompoundIndex(
        name = "unique_station_date_hour",
        def = "{ 'stationId': 1, 'measurementDate': 1, 'measurementHour': 1 }",
        unique = true
    )
)
data class WeatherMeasurementDocument(
    @Id
    val id: String? = null,

    @Indexed
    val stationId: String?,           // IMGW: id_stacji
    val stationName: String?,         // IMGW: stacja

    val measurementDate: String?,     // yyyy-MM-dd
    val measurementHour: String?,     // "00", "03", ...

    val temperature: String?,         // IMGW: temperatura
    val windSpeed: String?,           // predkosc_wiatru
    val windDirection: String?,       // kierunek_wiatru
    val relativeHumidity: String?,    // wilgotnosc_wzgledna
    val precipitation: String?,       // suma_opadu
    val pressure: String?,            // cisnienie
    val createdAt: Instant
)
