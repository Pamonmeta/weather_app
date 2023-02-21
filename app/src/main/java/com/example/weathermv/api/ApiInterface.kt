package com.example.weathermv.api

import com.example.weathermv.model.city.Countries
import com.example.weathermv.model.hour2.HourForecast
import com.example.weathermv.model.retroDay.DayForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v1/forecast")
    suspend fun getHour(@Query("latitude") latitude: Double,
                           @Query("longitude") longitude: Double,
                           @Query(value = "hourly",  encoded = true,) param: String,
                        @Query(value = "start_date",  encoded = true) start_date: String,
                        @Query(value = "end_date",  encoded = true) end_date: String
    ): Response<HourForecast>

    @GET("v1/forecast")
    suspend fun getDays(@Query("latitude") latitude: Double,
                        @Query("longitude") longitude: Double,
                        @Query(value = "daily",  encoded = true) param: String,
                        @Query(value = "timezone",  encoded = true) timezone: String,
                        @Query(value = "start_date",  encoded = true) start_date: String,
                        @Query(value = "end_date",  encoded = true) end_date: String
    ): Response<DayForecast>

}