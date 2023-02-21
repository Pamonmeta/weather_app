package com.example.weathermv

import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weathermv.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
//        val navController = findNavController(R.id.fragmentContainerView2)

//        bottomNavigationView.setupWithNavController(navController)

        binding.bottomNav?.setOnItemSelectedListener {
            var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
            when (it.itemId) {
                R.id.mainFragment -> {
                    fragment.view?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.mainFragment)
                    }
                }
                R.id.searhFragment -> fragment.view?.let { it1 ->
                    Navigation.findNavController(it1)
                        .navigate(R.id.searhFragment)
                }
                R.id.daysFragment2 -> fragment.view?.let { it1 ->
                    Navigation.findNavController(it1)
                        .navigate(R.id.daysFragment2)
                }
            }
            true
        }



    }



}