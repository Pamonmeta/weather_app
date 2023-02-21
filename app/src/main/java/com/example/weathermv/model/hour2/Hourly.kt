package com.example.weathermv.model.hour2

data class Hourly(
    val cloudcover: List<Int>,
    val pressure_msl: List<Double>,
    val relativehumidity_2m: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>,
    val windspeed_10m: List<Double>
)