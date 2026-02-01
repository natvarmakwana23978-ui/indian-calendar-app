package com.indian.calendar

import android.app.AlertDialog
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
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
        val tvMonthName: TextView = v.findViewById(R.id.tvMonthName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val data = items[position].allData
        val engDateFull = data.get("ENGLISH")?.asString ?: ""
        val localInfo = data.get(selectedLang)?.asString ?: ""

        // તારીખ સેટ કરવી
        holder.tvEnglishDate.text = if (engDateFull.contains("/")) engDateFull.split("/")[0] else engDateFull
        
        // ગુજરાતી તિથિ સેટ કરવી
        val parts = localInfo.split(" ")
        holder.tvTithi.text = if (parts.size > 1) parts[1] else ""

        // રવિવાર હોય તો લાલ રંગ
        if (localInfo.contains("રવિવાર")) {
            holder.tvEnglishDate.setTextColor(Color.RED)
            holder.tvTithi.setTextColor(Color.RED)
        } else {
            holder.tvEnglishDate.setTextColor(Color.BLACK)
            holder.tvTithi.setTextColor(Color.GRAY)
        }

        // તારીખ પર ટચ કરવાથી રિમાઇન્ડર મેનુ
        holder.itemView.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("રિમાઇન્ડર સેટ કરો")
            builder.setMessage("તારીખ: $engDateFull\nમાહિતી: $localInfo")
            builder.setPositiveButton("એડ / રીમુવ") { _, _ ->
                // અહીં આગળ આપણે ડેટાબેઝ ફોર્મ જોડીશું
            }
            builder.setNegativeButton("બંધ કરો", null)
            builder.show()
        }
    }

    override fun getItemCount(): Int = items.size
}
