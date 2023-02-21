package com.example.weathermv.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathermv.adapter.CityAdapter
import com.example.weathermv.api.ApiInterface
import com.example.weathermv.api.ApiUtility
import com.example.weathermv.databinding.FragmentSearhBinding
import com.example.weathermv.model.display.City
import com.example.weathermv.repository.HourRepository
import com.example.weathermv.viewmodel.HourViewModel
import com.example.weathermv.viewmodel.HoursViewModelFactory
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class SearhFragment : Fragment() {

    private var _binding: FragmentSearhBinding? = null
    private val binding get() = _binding!!

    private lateinit var hourViewModelC: HourViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentSearhBinding.inflate(inflater, container, false)

//        val apiInterfaceC = ApiUtility.getInstanceCity().create(ApiInterface::class.java)
//        val hourRepositoryC = HourRepository(apiInterfaceC)
//
//
//
//
//        hourViewModelC = ViewModelProvider(
//            this,
//            HoursViewModelFactory(hourRepositoryC)
//        ).get(HourViewModel::class.java)
//
//        hourViewModelC.getCities()
//
//
//        val recyclerviewD = binding.cityRV
//        recyclerviewD.layoutManager = LinearLayoutManager(context)
//
//
//        hourViewModelC.nonSort.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            Log.d("city","size - ${it.raw()}")
//        })
//
//        binding.searchView.clearFocus()
//
//        hourViewModelC.citiesSort.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//
//            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//                android.widget.SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String): Boolean {
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    var filteredCity = ArrayList<City>()
//                    for(city in it) {
//                        if(city.city.contains(newText!!)){
//                            filteredCity.add(city)
//                        }
//                    }
//                    if(filteredCity.isEmpty()){
//                        Log.d("city", "kaka popa")
//                    }else{
//                        val recyclerviewD = binding.cityRV
//                        val adapter = CityAdapter(filteredCity, requireContext())
//                        recyclerviewD.adapter = adapter
//                    }
//                    return true
//                }
//            })
//            Log.d("city","size - ${it.size}")
//            val adapter = CityAdapter(it, requireContext())
//            recyclerviewD.adapter = adapter
//        })

        val cityList: ArrayList<City> = ArrayList()

        try {
            val obj = JSONObject(getJSONFromAssets()!!)
            val citiesArray = obj.getJSONArray("cities")

            for (i in 0 until citiesArray.length()) {
                // Create a JSONObject for fetching single User's Data
                val cityit = citiesArray.getJSONObject(i)
                // Fetch id store it in variable
                val city = cityit.getString("city")
                val country = cityit.getString("country")

                // Now add all the variables to the data model class and the data model class to the array list.
                val cityAdd = City(country, city)

                cityList.add(cityAdd)
            }
        } catch (e: JSONException) {
            //exception
            e.printStackTrace()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var filteredCity = ArrayList<City>()
                for (city in cityList) {
                    if (city.city.lowercase().contains(newText!!.lowercase())) {
                        filteredCity.add(city)
                    }
                }
                if (filteredCity.isEmpty()) {
                    Log.d("city", "kaka popa")
                } else {
                    val recyclerviewD = binding.cityRV
                    val adapter = CityAdapter(filteredCity, binding.cityRV)
                    recyclerviewD.adapter = adapter
                }
                return true
            }
        })


        binding.cityRV.layoutManager = LinearLayoutManager(context)
        val itemAdapter = CityAdapter(cityList, binding.cityRV)
        binding.cityRV.adapter = itemAdapter



        return binding.root
    }

    private fun getJSONFromAssets(): String? {

        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val myUsersJSONFile = requireContext().assets.open("city.json")
            val size = myUsersJSONFile.available()
            val buffer = ByteArray(size)
            myUsersJSONFile.read(buffer)
            myUsersJSONFile.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

}