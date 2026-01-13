package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val daysList: List<String>,
    private val sheetDataMap: Map<String, CalendarDayData>,
    private val monthYearStr: String
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // તમે આપેલું ID: tvDay
        val tvDay: TextView = view.findViewById(R.id.tvDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daysList[position]
        holder.tvDay.text = day

        if (day.isNotEmpty()) {
            val dateParts = monthYearStr.split("/")
            val dateKey = "${dateParts[0]}/$day/${dateParts[1]}"
            val data = sheetDataMap[dateKey]
            
            if (data != null && data.detail.isNotEmpty()) {
                holder.tvDay.text = "$day\n${data.detail}"
            }
        }
    }

    override fun getItemCount() = daysList.size
}
