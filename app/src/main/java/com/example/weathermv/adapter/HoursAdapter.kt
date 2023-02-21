package com.example.weathermv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermv.R
import com.example.weathermv.model.display.DisplayHour

class HoursAdapter(private val dList: List<DisplayHour>,
                   private val cloud: TextView,
                   private val humidity: TextView,
                   private val pressure: TextView,
                   private val todayCard: CardView): RecyclerView.Adapter<HoursAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return dList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hour_row,parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dList[position]

        //holder.date.text =  data.time.substring(0,10)
        holder.time1.text = data.time.substring(11,16)
        holder.celsium.text =  ""+ data.temperature_2m + "℃ "
        holder.icon.setImageResource(data.weatherType.iconRes)

        holder.card_hour.setOnClickListener {
            todayCard.visibility = VISIBLE

            cloud.text = "Zachmurzenie:" + data.cloudcover.toString() + "%"
            humidity.text = "Wilgotność:" + data.relativehumidity_2m.toString() + "%"
            pressure.text = "Ciśnienie:" + data.pressure_msl.toString() + "hpa"
        }
        //holder.humidity.text = "Humidity: "+ data.relativehumidity_2m
        //holder.cloud.text = "Cloud: "+ data.cloudcover + "%"
        //holder.pres.text = "Pressure: "+ data.pressure_msl + "Pa"


    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        //val date: TextView = view.findViewById(R.id.date)
        val time1: TextView = view.findViewById(R.id.time)
        val celsium: TextView = view.findViewById(R.id.celsium)
        val icon: ImageView = view.findViewById(R.id.iconW)
        val card_hour: CardView = view.findViewById(R.id.card_hour)
    }
}