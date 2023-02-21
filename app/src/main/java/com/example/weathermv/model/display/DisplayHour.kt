package com.example.weathermv.model.display

import com.example.weathermv.weathercode.WeatherType

data class DisplayHour(
    val cloudcover: Int,
    val pressure_msl: Double,
    val relativehumidity_2m: Int,
    val temperature_2m: Double,
    val time: String,
    val weatherType: WeatherType,
    val windspeed: Double
)
