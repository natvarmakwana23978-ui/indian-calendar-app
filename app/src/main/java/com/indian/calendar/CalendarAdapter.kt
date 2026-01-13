package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val days: List<String>, 
    private val sheetData: Map<String, CalendarDayData> // તારીખ મુજબ ડેટા
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDetail: TextView = view.findViewById(R.id.tvDetail) // તિથિ માટે
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        holder.tvDate.text = day
        
        if (day.isNotEmpty()) {
            // અહીં તમારી શીટના ડેટા સાથે તારીખ મેચ કરવામાં આવશે
            // ઉદાહરણ તરીકે: "1/1/2026"
            val detail = sheetData[day]?.detail ?: ""
            holder.tvDetail.text = detail
        }
    }

    override fun getItemCount() = days.size
}
