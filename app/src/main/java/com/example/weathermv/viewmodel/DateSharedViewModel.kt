package com.example.weathermv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DateSharedViewModel:ViewModel() {
    private var _dateDetail = MutableLiveData("2022-12-28")
    val dateDetail:LiveData<String> = _dateDetail

    fun saveDate(newDate: String){
        _dateDetail.value = newDate
    }
}