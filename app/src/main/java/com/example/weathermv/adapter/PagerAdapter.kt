package com.example.weathermv.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weathermv.fragments.DayDetailsFragment
import com.example.weathermv.fragments.details.*

class PagerAdapter(fragmentActivity: DayDetailsFragment): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->TemperatureFragment()
            1 ->CloudFragment()
            2 ->HumidityFragment()
            3 ->WindspeedFragment()
            else -> PressureFragment()
        }
    }
}