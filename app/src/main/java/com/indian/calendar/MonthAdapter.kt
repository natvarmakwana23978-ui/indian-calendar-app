package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MonthAdapter(
    private val items: List<CalendarDayData>, // JsonObject ને બદલે આ ટાઈપ વાપરો
    private val selectedLang: String
) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    class MonthViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val day = items[position]
        holder.tvDate.text = day.date
        
        // ભાષા મુજબ વિગત મેળવો
        val tithi = day.details?.get(selectedLang)?.asString ?: ""
        holder.tvTithi.text = tithi

        // રવિવાર અથવા રજા માટે લાલ રંગ
        if (day.color_code == 1 || day.isSunday) {
            holder.tvDate.setTextColor(Color.RED)
            holder.tvTithi.setTextColor(Color.RED)
        } else {
            holder.tvDate.setTextColor(Color.BLACK)
            holder.tvTithi.setTextColor(Color.GRAY)
        }
    }

    override fun getItemCount(): Int = items.size
}
