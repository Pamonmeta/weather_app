package com.example.weathermv.fragments.details

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.weathermv.R
import com.example.weathermv.api.ApiInterface
import com.example.weathermv.api.ApiUtility
import com.example.weathermv.databinding.FragmentTemperatureDayBinding
import com.example.weathermv.databinding.FragmentWindspeedBinding
import com.example.weathermv.repository.HourRepository
import com.example.weathermv.viewmodel.DateSharedViewModel
import com.example.weathermv.viewmodel.HourViewModel
import com.example.weathermv.viewmodel.HoursViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

class WindspeedFragment : Fragment() {
    private var _binding: FragmentWindspeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var hourViewModel: HourViewModel
    val entries = ArrayList<Entry>()
    private val sharedViewModel: DateSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentWindspeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val hourRepository = HourRepository(apiInterface)

        val current = LocalDateTime.now()
        val formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = current.format(formatterDay)

        val period = Period.of(0, 0, 14)
        val date = LocalDate.parse(startDate)

        val endDate = date.plus(period).toString()

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
            hourViewModel.getHours(
                it.latitude,
                it.longitude,
                "temperature_2m,pressure_msl,cloudcover,relativehumidity_2m,weathercode,windspeed_10m",
                startDate,
                endDate
            )
        }

        hourViewModel.hourSort.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            sharedViewModel.dateDetail.observe(viewLifecycleOwner) { date ->
                for (i in 0..it.size - 1) {
                    if (it[i].time.substring(0, 10) == date) {
                        entries.add(
                            Entry(
                                it[i].time.substring(11, 13).toFloat(),
                                it[i].windspeed.toFloat()
                            )
                        )
                    }
                }
                val lineDataSet = LineDataSet(entries, "Hours")

                lineDataSet.apply {
                    color = R.color.black
                    setDrawValues(false)
                    lineWidth = 2f
                    isHighlightEnabled = true
                    setDrawHighlightIndicators(false)
                    setDrawCircles(false)
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                }
                binding.lineChart.data = LineData(lineDataSet)
                //binding.lineChart.animateX(500, Easing.EaseInExpo)

                binding.lineChart.apply {
                    axisRight.isEnabled = false

                    axisLeft.apply {
                        //isEnabled = false
//                    axisMinimum = -10f
//                    axisMinimum = 10f
                        isGranularityEnabled = true
                        setDrawGridLines(false)
                    }
//
                    xAxis.apply {
                        axisMinimum = 0f
                        axisMaximum = 24f
                        isGranularityEnabled = true
                        //granularity = 4f
                        setDrawGridLines(false)
                        setDrawLimitLinesBehindData(false)
                        setDrawAxisLine(false)
                        position = XAxis.XAxisPosition.BOTTOM
                    }
                    setTouchEnabled(true)
                    isDragEnabled = true
                    setScaleEnabled(false)
                    setPinchZoom(false)
//                description = null
//                legend.isEnabled = false
                    invalidate()
                }
            }
        })
    }
}