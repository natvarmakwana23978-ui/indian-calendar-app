package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val items: List<CalendarDayData>, private val selectedLang: String) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {
    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val data = items[position].allData
        val engDateFull = data.get("ENGLISH")?.asString ?: ""
        holder.tvEnglishDate.text = if (engDateFull.contains("/")) engDateFull.split("/")[0] else engDateFull
        
        val localInfo = data.get(selectedLang)?.asString ?: ""
        holder.tvTithi.text = localInfo

        if (localInfo.contains("રવિવાર") || localInfo.contains("Sunday")) {
            holder.tvEnglishDate.setTextColor(Color.RED)
            holder.tvTithi.setTextColor(Color.RED)
        } else {
            holder.tvEnglishDate.setTextColor(Color.BLACK)
            holder.tvTithi.setTextColor(Color.GRAY)
        }
    }
    override fun getItemCount(): Int = items.size
}
