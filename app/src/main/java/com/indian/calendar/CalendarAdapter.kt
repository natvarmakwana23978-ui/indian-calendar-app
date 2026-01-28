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
        
        // જો ખાલી ખાનું (null) હોય તો
        if (day == null) {
            holder.tvDate.text = ""
            holder.tvLocal.text = ""
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            return
        }

        // તારીખ સેટ કરો
        holder.tvDate.text = day.englishDate.substringBefore("/")
        val localData = day.allData.get(selectedLang)?.asString ?: ""
        holder.tvLocal.text = localData

        // --- કલર કોડિંગ લોજિક (પગલું ૮ મુજબ) ---
        
        val rawData = day.allData.toString()
        val isSunday = position % 7 == 0 // રવિવાર હંમેશા પહેલા ખાનામાં (૦, ૭, ૧૪...)

        when {
            // ૧. શનિ-રવિ અને રજાઓ (લાલ રંગ)
            isSunday || rawData.contains("Sun") || rawData.contains("New Year") -> {
                holder.tvDate.setTextColor(Color.RED)
            }
            // ૨. હિન્દુ તહેવારો (કેસરી રંગ - સુદ/વદ ના આધારે)
            rawData.contains("સુદ") || rawData.contains("વદ") -> {
                holder.tvDate.setTextColor(Color.parseColor("#FF8C00")) 
            }
            // ૩. મુસ્લિમ તહેવાર (લીલો રંગ - ઇસ્લામિક ડેટાના આધારે)
            selectedLang.contains("Islamic") || rawData.contains("Rajab") -> {
                holder.tvDate.setTextColor(Color.parseColor("#008000"))
            }
            // ૪. બાકીના દિવસો (કાળો રંગ)
            else -> {
                holder.tvDate.setTextColor(Color.BLACK)
            }
        }
    }

    override fun getItemCount() = days.size
}
