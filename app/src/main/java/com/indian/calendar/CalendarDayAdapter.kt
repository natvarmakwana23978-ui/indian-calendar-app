package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData

class CalendarDayAdapter(
    private val days: List<CalendarDayData>,
    private val colIndex: Int, // પસંદ કરેલી ભાષાનો ઇન્ડેક્સ
    private val onDayClick: (CalendarDayData) -> Unit
) : RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // તારીખ સેટ કરો (દા.ત. 1/1/2026 માંથી '1' અલગ કરો)
        holder.txtDate.text = day.Date.split("/").getOrNull(0) ?: ""

        // પસંદ કરેલી ભાષા (colIndex) મુજબ ડેટા બતાવો
        holder.txtTithi.text = when(colIndex) {
            1 -> day.Gujarati
            2 -> day.Hindi
            3 -> day.Islamic
            4 -> day.TeluguKannada
            10 -> day.Nepali
            // બાકીની બધી ૨૬ ભાષાઓ આ રીતે ઉમેરી શકાય...
            else -> day.Gujarati
        }
        
        holder.itemView.setOnClickListener { onDayClick(day) }
    }

    override fun getItemCount() = days.size
}
