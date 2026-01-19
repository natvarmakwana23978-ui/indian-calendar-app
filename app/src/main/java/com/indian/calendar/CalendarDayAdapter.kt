package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData

class CalendarDayAdapter(
    private val days: List<CalendarDayData>,
    private val colIndex: Int,
    private val onDayClick: (CalendarDayData) -> Unit
) : RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // પૂરી તારીખ બતાવો કારણ કે આ વર્ટિકલ લિસ્ટ છે [cite: 2026-01-07]
        holder.txtDate.text = day.Date 

        // પસંદ કરેલી ભાષા મુજબ ડેટા
        holder.txtTithi.text = when(colIndex) {
            1 -> day.Gujarati
            2 -> day.Hindi
            10 -> day.Nepali
            else -> day.Gujarati
        }
        
        holder.itemView.setOnClickListener { onDayClick(day) }
    }

    override fun getItemCount() = days.size
}
