package com.example.weathermv.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.weathermv.R
import com.example.weathermv.adapter.PagerAdapter
import com.example.weathermv.api.ApiInterface
import com.example.weathermv.api.ApiUtility
import com.example.weathermv.databinding.FragmentDayDetailsBinding
import com.example.weathermv.fragments.details.TemperatureFragment
import com.example.weathermv.repository.HourRepository
import com.example.weathermv.viewmodel.DateSharedViewModel
import com.example.weathermv.viewmodel.HourViewModel
import com.example.weathermv.viewmodel.HoursViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.google.android.material.tabs.TabLayoutMediator

class DayDetailsFragment : Fragment() {

    private var _binding: FragmentDayDetailsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: DateSharedViewModel by activityViewModels()

    val args: DayDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentDayDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initial()
        val date = args.dateday
        sharedViewModel.saveDate(date)

    }

    private fun initial() {
        binding.viewPager.adapter = PagerAdapter(this)
        binding.tabLayout.tabIconTint = null
        TabLayoutMediator(binding.tabLayout, binding.viewPager){
                tab, pos ->
            when(pos){
                0 -> {
                    tab.setIcon(R.drawable.ic_baseline_device_thermostat_24)
                    tab.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.orange))
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_cloudy)
                    tab.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.blue))
                }
                2 -> {
                    tab.setIcon(R.drawable.ic_drop)
                    tab.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.bluedrop))
                }
                3 -> {
                    tab.setIcon(R.drawable.ic_wind)
                    tab.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.grey))
                }
                4 -> {
                    tab.setIcon(R.drawable.ic_pressure)
                    tab.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
        }.attach()
    }


}