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
        // ખાતરી કરજો કે item_calendar_day.xml માં ટેક્સ્ટ કલર કાળો (#000000) છે
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // ENGLISH કોલમમાંથી તારીખ (દા.ત. 1/1/2026 માંથી '1')
        val dateParts = day.Date.split("/")
        holder.txtDate.text = if (dateParts.isNotEmpty()) dateParts[0] else ""

        // પસંદ કરેલી ભાષા (colIndex) મુજબ ડેટા બતાવવાનું લોજિક [cite: 2026-01-07]
        holder.txtTithi.text = when(colIndex) {
            1 -> day.Gujarati
            2 -> day.Hindi
            3 -> day.Islamic
            4 -> day.TeluguKannada
            5 -> day.Tamil
            6 -> day.Malayalam
            7 -> day.Punjabi
            8 -> day.Odia
            9 -> day.Bengali
            10 -> day.Nepali
            11 -> day.Chinese
            12 -> day.Hebrew
            13 -> day.Persian
            14 -> day.Ethiopian
            15 -> day.Balinese
            16 -> day.Korean
            17 -> day.Vietnamese
            18 -> day.Thai
            19 -> day.French
            20 -> day.Burmese
            21 -> day.Kashmiri
            22 -> day.Marwari
            23 -> day.Japanese
            24 -> day.Assamese
            25 -> day.Sindhi
            26 -> day.Tibetan
            else -> day.Gujarati
        }
        
        holder.itemView.setOnClickListener { onDayClick(day) }
    }

    override fun getItemCount() = days.size
}
