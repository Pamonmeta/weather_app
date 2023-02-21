package com.example.weathermv.model.display

import com.example.weathermv.weathercode.WeatherType

data class DisplayDay(
    val date: String,
    val tmax: Double,
    val tmin: Double,
    val sunrise: String,
    val sunset: String,
    val weatherCode: WeatherType
)
