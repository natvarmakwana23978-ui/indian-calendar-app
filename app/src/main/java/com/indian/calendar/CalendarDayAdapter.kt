package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData

class CalendarDayAdapter(private val dayList: List<CalendarDayData>) :
    RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEnglishDate: TextView = view.findViewById(R.id.txtEnglishDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
        val txtFestival: TextView = view.findViewById(R.id.txtFestival)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dayList[position]
        
        // તારીખ બતાવવી (તમારી શીટમાં જે રીતે હોય તે મુજબ)
        holder.txtEnglishDate.text = day.Date
        holder.txtTithi.text = day.Tithi
        holder.txtFestival.text = day.Festival_English

        // રવિવાર માટે લાલ રંગ સેટ કરવાની સાચી રીત
        if (day.Day.contains("Sun", ignoreCase = true) || day.Day.contains("રવિ", ignoreCase = true)) {
            holder.txtEnglishDate.setTextColor(Color.RED)
        } else {
            holder.txtEnglishDate.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int = dayList.size
}
