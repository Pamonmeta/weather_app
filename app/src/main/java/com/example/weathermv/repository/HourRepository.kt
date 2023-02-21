package com.example.weathermv.repository

import com.example.weathermv.api.ApiInterface
import com.example.weathermv.model.city.Countries
import com.example.weathermv.model.hour2.HourForecast
import com.example.weathermv.model.retroDay.DayForecast
import retrofit2.Response

class HourRepository(private val apiInterface: ApiInterface) {

    //private val hourLiveData = MutableLiveData<ForecastData>()

//    val hours : LiveData<ForecastData>
//    get() = hourLiveData
//
//    suspend fun getHours(){
//        val result = apiInterface.getHourlyForecast()
//        if(result.body() != null){
//            hourLiveData.postValue(result.body())
//        }
//    }

    suspend fun getHour(
        latitude: Double,
        longitude: Double,
        param: String,
        start: String,
        end: String): Response<HourForecast> {
        return apiInterface.getHour(latitude,longitude,param,start, end)
    }

    suspend fun getDays(latitude: Double,
                        longitude: Double,
                        param: String,
                        timezone: String,
                        start_date: String,
                        end_date: String): Response<DayForecast> {
        return apiInterface.getDays(latitude,longitude,param, timezone, start_date, end_date)
    }

}