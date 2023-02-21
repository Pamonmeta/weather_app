package com.example.weathermv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermv.R
import com.example.weathermv.fragments.SearhFragmentDirections
import com.example.weathermv.model.display.City

class CityAdapter(private var dList: List<City>, private val view1: RecyclerView): RecyclerView.Adapter<CityAdapter.MyViewHolder>() {

    fun filterList(fList: List<City>){
        dList = fList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_row,parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dList[position]

        holder.city.text =  data.city
        holder.country.text =  data.country
        holder.conL.setOnClickListener{
            val acc = SearhFragmentDirections.searchToCity(data.city)
            val action = SearhFragmentDirections.searchToCity(data.city)
            Navigation.findNavController(view1).navigate(acc)
        }

    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val conL: ConstraintLayout = view.findViewById(R.id.cityLayout)
        val city: TextView = view.findViewById(R.id.city)
        val country: TextView = view.findViewById(R.id.country)
    }
}