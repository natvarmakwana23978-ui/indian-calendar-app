package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

class CalendarAdapter(
    private val items: List<CalendarDayData>,
    private val selectedLang: String
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvMonthName: TextView = v.findViewById(R.id.tvMonthName)
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
        val tvFestival: TextView = v.findViewById(R.id.tvFestival)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val data = items[position].allData
        val localData = data.get(selectedLang)?.asString ?: ""
        val parts = localData.split(" ")
        
        val monthName = if (parts.isNotEmpty()) parts[0] else ""
        val tithiOnly = if (parts.size > 1) parts[1] else ""

        // ૧. ઉપરનું લખાણ (અમાસ અથવા મહિનાનું નામ)
        if (tithiOnly.contains("વદ-૩૦") || tithiOnly.contains("અમાસ")) {
            holder.tvMonthName.text = "અમાસ"
            holder.tvMonthName.visibility = View.VISIBLE
        } else if (tithiOnly.contains("સુદ-૧") || position == 0) {
            holder.tvMonthName.text = monthName
            holder.tvMonthName.visibility = View.VISIBLE
        } else {
            holder.tvMonthName.visibility = View.GONE
        }

        // ૨. અંગ્રેજી તારીખ
        val engDate = data.get("ENGLISH")?.asString ?: ""
        holder.tvEnglishDate.text = if (engDate.contains("/")) engDate.split("/")[0] else engDate

        // ૩. નીચે તિથિ અને રવિવારનો રંગ
        holder.tvTithi.text = tithiOnly
        if (localData.contains("રવિવાર")) {
            holder.tvEnglishDate.setTextColor(Color.RED)
            holder.tvTithi.setTextColor(Color.RED)
        } else {
            holder.tvEnglishDate.setTextColor(Color.BLACK)
            holder.tvTithi.setTextColor(Color.GRAY)
        }
    }

    override fun getItemCount(): Int = items.size
}
