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
        // ખાતરી કરો કે તમારા item_calendar_day.xml માં આ ID છે
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDetail: TextView = view.findViewById(R.id.tvDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daysList[position]
        holder.tvDate.text = day

        if (day.isNotEmpty()) {
            // શીટની તારીખ (M/d/yyyy) સાથે મેચ કરવા માટે
            val fullDateKey = "$monthYearStr".replaceBefore("/", day).replace("/", "/$day/")
            // જો ઉપરનું લોજિક અઘરું લાગે તો સીધું:
            val dateKey = "${monthYearStr.split("/")[0]}/$day/${monthYearStr.split("/")[1]}"
            
            val data = sheetDataMap[dateKey]
            holder.tvDetail.text = data?.detail ?: ""
        } else {
            holder.tvDetail.text = ""
        }
    }

    override fun getItemCount() = daysList.size
}
