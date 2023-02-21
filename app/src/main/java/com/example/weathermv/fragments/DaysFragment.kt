package com.example.weathermv.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathermv.adapter.DaysAdapter
import com.example.weathermv.api.ApiInterface
import com.example.weathermv.api.ApiUtility
import com.example.weathermv.databinding.FragmentDaysBinding
import com.example.weathermv.repository.HourRepository
import com.example.weathermv.viewmodel.HourViewModel
import com.example.weathermv.viewmodel.HoursViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

class DaysFragment : Fragment() {

    private var _binding: FragmentDaysBinding? = null
    private val binding get() = _binding!!

    private lateinit var hourViewModel: HourViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentDaysBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val hourRepository = HourRepository(apiInterface)

        val current = LocalDateTime.now()
        val formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = current.format(formatterDay)

        var period = Period.of(0, 0, 14)
        var date = LocalDate.parse(startDate)

        var endDate = date.plus(period).toString()

        hourViewModel = ViewModelProvider(
            this,
            HoursViewModelFactory(hourRepository)
        ).get(HourViewModel::class.java)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }

        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            hourViewModel.getDays(
                it.latitude,
                it.longitude,
                "temperature_2m_max,temperature_2m_min,sunrise,sunset,weathercode", "Europe/Berlin",
                startDate,
                endDate
            )
        }

        val recyclerviewD = binding.daysRV
        recyclerviewD.layoutManager = LinearLayoutManager(context)

        hourViewModel.daysSort.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                Log.d("exemple", "lista posle ${it.size.toString()}")
                val adapter = DaysAdapter(it, recyclerviewD)
                recyclerviewD.adapter = adapter
            }
        })

        hourViewModel.daysSort.postValue(null)
    }

}