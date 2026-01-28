package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val days: List<CalendarDayData?>,
    private val selectedLang: String
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocal: TextView = v.findViewById(R.id.tvLocalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        if (day == null) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.tvDate.text = ""
            holder.tvLocal.text = ""
            return
        }

        holder.tvDate.text = day.englishDate.substringBefore("/")
        val localInfo = day.allData.get(selectedLang)?.asString ?: ""
        holder.tvLocal.text = localInfo

        val raw = day.allData.toString()
        val isSunday = position % 7 == 0

        when {
            // રવિવાર અથવા રજા: લાલ બેકગ્રાઉન્ડ, સફેદ ફોન્ટ
            isSunday || raw.contains("Holiday") || raw.contains("New Year") -> {
                holder.itemView.setBackgroundResource(android.R.color.white) // Default safety
                holder.itemView.setBackgroundColor(Color.RED)
                holder.tvDate.setTextColor(Color.WHITE)
                holder.tvLocal.setTextColor(Color.WHITE)
            }
            // હિન્દુ તહેવાર (સુદ/વદ): કેસરી બેકગ્રાઉન્ડ, સફેદ ફોન્ટ
            raw.contains("સુદ") || raw.contains("વદ") -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00")) 
                holder.tvDate.setTextColor(Color.WHITE)
                holder.tvLocal.setTextColor(Color.WHITE)
            }
            // સામાન્ય દિવસ: સફેદ બેકગ્રાઉન્ડ, કાળા ફોન્ટ
            else -> {
                holder.itemView.setBackgroundColor(Color.WHITE)
                holder.tvDate.setTextColor(Color.BLACK)
                holder.tvLocal.setTextColor(Color.BLACK)
            }
        }
    }

    override fun getItemCount(): Int = days.size
}
