package com.example.weathermv.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermv.R
import com.example.weathermv.fragments.DaysFragmentDirections
import com.example.weathermv.model.display.DisplayDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DaysAdapter(private val dList: List<DisplayDay>, private val view1: RecyclerView): RecyclerView.Adapter<DaysAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
            return dList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_row,parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dList[position]

        val string = data.date.substring(0,10)
        val date = LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
        val roraF = DateTimeFormatter.ofPattern("dd MMMM", Locale("pl"))
        val roraW = DateTimeFormatter.ofPattern("EEEE", Locale("pl"))
        val ror = date.format(roraF)
        val rorW = date.format(roraW).capitalize()

        holder.week.text =  rorW
        holder.date.text =  ror
        holder.tmax.text = "max: " + data.tmax.toString() + "℃"
        holder.tmin.text =  "min: " + data.tmin.toString() + "℃"

        holder.icon.setImageResource(data.weatherCode.iconRes)
        holder.conL.setOnClickListener{
            val action = DaysFragmentDirections.dayToTab(data.date.substring(0,10))
            Navigation.findNavController(view1).navigate(action)
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val conL: ConstraintLayout = view.findViewById(R.id.dayLayout)
        val date: TextView = view.findViewById(R.id.month)
        val week: TextView = view.findViewById(R.id.week)
        val tmax: TextView = view.findViewById(R.id.tmax)
        val tmin: TextView = view.findViewById(R.id.tmin)
        val icon: ImageView = view.findViewById(R.id.iconW)
    }
}