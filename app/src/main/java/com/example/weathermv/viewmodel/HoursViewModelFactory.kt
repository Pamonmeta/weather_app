package com.example.weathermv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathermv.repository.HourRepository

class HoursViewModelFactory(private val hourRepository: HourRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HourViewModel(hourRepository) as T
    }
}