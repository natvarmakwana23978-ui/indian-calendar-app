package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarDayAdapter(private val days: List<CalendarDayData>) :
    RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEnglishDate: TextView = view.findViewById(R.id.txtEnglishDate)
        val txtLocalDate: TextView = view.findViewById(R.id.txtLocalDate)
        val alertLayout: View = view.findViewById(R.id.alertLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // ઇંગ્લિશ તારીખ સેટ કરો [cite: 2026-01-21]
        holder.txtEnglishDate.text = day.date
        
        // આ લાઈન હવે દરેક ભાષા (Gujarati, Hindi વગેરે) માટે ઓટોમેટિક કામ કરશે [cite: 2026-01-21]
        holder.txtLocalDate.text = day.localDate

        // જો Alert હોય તો જ લાલ પટ્ટી બતાવવી [cite: 2026-01-17, 2026-01-21]
        if (!day.alert.isNullOrEmpty()) {
            holder.alertLayout.visibility = View.VISIBLE
            holder.alertLayout.setBackgroundColor(Color.RED)
        } else {
            holder.alertLayout.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
