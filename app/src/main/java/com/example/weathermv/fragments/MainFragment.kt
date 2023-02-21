package com.example.weathermv.fragments

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermv.R
import com.example.weathermv.adapter.HoursAdapter
import com.example.weathermv.api.ApiInterface
import com.example.weathermv.api.ApiUtility
import com.example.weathermv.databinding.FragmentMainBinding
import com.example.weathermv.repository.HourRepository
import com.example.weathermv.viewmodel.HourViewModel
import com.example.weathermv.viewmodel.HoursViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var hourViewModel: HourViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val hourRepository = HourRepository(apiInterface)

        hourViewModel = ViewModelProvider(
            this,
            HoursViewModelFactory(hourRepository)
        ).get(HourViewModel::class.java)


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
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it != null) {
                val current = LocalDateTime.now()
                val formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val startDate = current.format(formatterDay)

                val roraF = DateTimeFormatter.ofPattern("EEEE dd MMMM", Locale("en"))
                val ror = current.format(roraF)

                Log.d("date", "Local - $ror ")


                val period = Period.of(0, 0, 14)
                val date = LocalDate.parse(startDate)
                val endDate = date.plus(period)
                val pisyaKaka = endDate.format(formatterDay)

                hourViewModel.getHours(
                    it.latitude,
                    it.longitude,
                    "temperature_2m,pressure_msl,cloudcover,relativehumidity_2m,weathercode,windspeed_10m",
                    startDate,
                    pisyaKaka
                )
                Log.d("exemple", "Latitude: ${it.latitude} Longitude: ${it.longitude}")
                val adress = geoCoder.getFromLocation(it.latitude, it.longitude, 1)
                val cityName = adress.get(0).locality
                binding.tvCity.text = cityName
            } else {
                Log.d("exemple", "cisdfsdfssefsty fun ")
            }
        }

        val current1 = LocalDateTime.now()
        val formatterDay1 = DateTimeFormatter.ofPattern("HH")
        val hhCurrent = current1.format(formatterDay1).toInt()

        binding.cardTodayDetails.visibility = GONE

        val recyclerviewH = binding.hoursRV
        recyclerviewH.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        hourViewModel.hourSort.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                val slice = it.slice(hhCurrent..47)
                binding.tvCelsium.text =
                    it[hhCurrent].temperature_2m.toString().substringBeforeLast(".") + "°"
                binding.status.text = it[hhCurrent].weatherType.weatherDesc
                binding.pa.text =
                    it[hhCurrent].pressure_msl.toString().substringBeforeLast(".") + " hpa"
                binding.wl.text = it[hhCurrent].relativehumidity_2m.toString() + " %"
                binding.wind.text =
                    it[hhCurrent].windspeed.toString().substringBeforeLast(".") + " km/h"

                val adapter = HoursAdapter(
                    slice, binding.todayCloud,
                    binding.todayHumidity,
                    binding.todayPressure,
                    binding.cardTodayDetails
                )

                recyclerviewH.adapter = adapter
            } else {
                Toast.makeText(context, "PIZDEC", LENGTH_LONG).show()
            }

            binding.tcMorning.text = it[7].temperature_2m.toString() + "°"
            binding.tcAfternoon.text = it[13].temperature_2m.toString() + "°"
            binding.tcEvening.text = it[18].temperature_2m.toString() + "°"
            binding.tcNight.text = it[23].temperature_2m.toString() + "°"

            mainGraph(
                it[7].temperature_2m.toString(),
                it[13].temperature_2m.toString(),
                it[18].temperature_2m.toString(),
                it[23].temperature_2m.toString()
            )
        })

    }

    private fun mainGraph(one: String, two: String, three: String, four: String) {
        val entries = ArrayList<Entry>()

        entries.add(Entry(7f, one.toFloat()))
        entries.add(Entry(13f, two.toFloat()))
        entries.add(Entry(18f, three.toFloat()))
        entries.add(Entry(23f, four.toFloat()))

        Log.d("graph", "lista posle ${four.toFloat()}")
        Log.d("graph", "lista posle ${entries.size.toString()}")

        val lineDataSet = LineDataSet(entries, "Hours")

        lineDataSet.apply {
            color = R.color.black
            setDrawValues(false)
            lineWidth = 2f
            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
            setDrawCircles(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        binding.mainChart.data = LineData(lineDataSet)
        binding.mainChart.apply {
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            xAxis.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
            setPinchZoom(false)
            description = null
            legend.isEnabled = false
            invalidate()
        }
    }

}