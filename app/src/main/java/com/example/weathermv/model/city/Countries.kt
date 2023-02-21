package com.example.weathermv.model.city

data class Countries(
    val `data`: List<Data>,
    val error: Boolean,
    val msg: String
)