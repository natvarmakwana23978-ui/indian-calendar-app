package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val days: List<CalendarDayData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
        val tvAlert: TextView = v.findViewById(R.id.tvAlert)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayData = days[position]
        
        // ૧. ઓફસેટ (ખાલી) દિવસોને છુપાવો [cite: 2026-01-21]
        if (dayData.englishDate.isNullOrEmpty()) {
            holder.itemView.visibility = View.INVISIBLE
            return
        }
        holder.itemView.visibility = View.VISIBLE
        
        // ૨. માત્ર અંગ્રેજી તારીખનો આંકડો સેટ કરો [cite: 2026-01-21]
        val parts = dayData.englishDate.split(" ")
        holder.tvEng.text = if (parts.size >= 3) parts[2] else ""
        
        // ૩. ગુજરાતી તિથિ સેટ કરો [cite: 2026-01-21]
        holder.tvLoc.text = dayData.localDate ?: ""

        // ૪. લાલ પટ્ટી ફિલ્ટર (માત્ર ભારતીય તહેવારો બતાવવા માટે) [cite: 2026-01-21]
        val alertText = dayData.alert ?: ""
        val filterWords = listOf(
            "Tevet", "Shevat", "Adar", "Nisan", "Iyar", "Sivan", 
            "Tamuz", "Av", "Elul", "Tishrei", "Chesh", "Kislev",
            "Rajab", "Shaban", "Ramadan", "Shawwal"
        )
        
        val isInvalid = filterWords.any { alertText.contains(it, ignoreCase = true) }

        if (alertText.isNotEmpty() && !isInvalid) {
            holder.tvAlert.visibility = View.VISIBLE
            holder.tvAlert.text = alertText
        } else {
            holder.tvAlert.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
