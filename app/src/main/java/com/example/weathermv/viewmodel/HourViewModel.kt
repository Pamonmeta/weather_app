package com.example.weathermv.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermv.model.city.Countries
import com.example.weathermv.model.display.City
import com.example.weathermv.model.display.DisplayDay
import com.example.weathermv.model.display.DisplayHour
import com.example.weathermv.repository.HourRepository
import com.example.weathermv.weathercode.WeatherType
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HourViewModel(private val hourRepository: HourRepository):ViewModel() {

    var hourSort: MutableLiveData<List<DisplayHour>> = MutableLiveData()
    var daysSort: MutableLiveData<List<DisplayDay>?> = MutableLiveData()

    var date11: MutableLiveData<String> = MutableLiveData()

//    init {
//        viewModelScope.launch (Dispatchers.IO){
//            hourRepository.getHours()
//        }
//    }

//    val hours: LiveData<ForecastData>
//    get() = hourRepository.hours

        fun getHours(
            latitude: Double,
            longitude: Double,
            param: String,
            start: String,
            end: String
        ) {
            var hoursList = ArrayList<DisplayHour>()
            viewModelScope.launch {
                val response = hourRepository.getHour(latitude, longitude, param, start, end)
                val hours = response.body()?.hourly
                for (i in 0..hours?.cloudcover!!.size - 1) {
                    hoursList.add(
                        DisplayHour(
                            cloudcover = hours.cloudcover[i],
                            pressure_msl = hours.pressure_msl[i],
                            relativehumidity_2m = hours.relativehumidity_2m[i],
                            temperature_2m = hours.temperature_2m[i],
                            time = hours.time[i],
                            weatherType = WeatherType.fromWMO(hours.weathercode[i]),
                            windspeed = hours.windspeed_10m[i]
                        )
                    )
                }
                hourSort.value = hoursList
            }
        }

        fun getDays(
            latitude: Double,
            longitude: Double,
            param: String,
            timezone: String,
            start_date: String,
            end_date: String
        ) {
            daysSort.postValue(null)
            var daysList = ArrayList<DisplayDay>()
            viewModelScope.launch {
                val response = hourRepository.getDays(latitude, longitude, param, timezone, start_date, end_date)
                val days = response.body()?.daily
                for (i in 0..days?.sunrise!!.size - 1) {
                    daysList.add(
                        DisplayDay(
                            date = days.time[i],
                            tmax = days.temperature_2m_max[i],
                            tmin = days.temperature_2m_min[i],
                            sunrise = days.sunrise[i],
                            sunset = days.sunset[i],
                            weatherCode = WeatherType.fromWMO(days.weathercode[i])
                        )
                    )
                }
                Log.d("exemple", "lista czsowwwww ${daysList.size.toString()}")
                daysSort.value = daysList
            }
        }

}