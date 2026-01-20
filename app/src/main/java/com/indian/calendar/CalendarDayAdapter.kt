package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarDayAdapter(
    private val days: List<CalendarDayData>, 
    private val colIndex: Int
) : RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
        val txtAlertBanner: TextView = view.findViewById(R.id.txtAlertBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        holder.txtDate.text = day.Date
        holder.txtDate.setTextColor(Color.BLACK)

        // સિલેક્ટ કરેલા કેલેન્ડર મુજબ તિથિ સેટ થશે
        val tithiText = when(colIndex) {
            1 -> day.Gujarati
            2 -> day.Hindi
            3 -> day.Marathi
            4 -> day.Sanskrit
            else -> day.Gujarati
        }
        
        holder.txtTithi.text = tithiText
        holder.txtTithi.setTextColor(Color.BLACK)

        // એલર્ટ લોજિક
        if (day.Alert.isNotEmpty()) {
            holder.txtAlertBanner.visibility = View.VISIBLE
            holder.txtAlertBanner.text = day.Alert
            holder.txtAlertBanner.setTextColor(Color.WHITE)
        } else {
            holder.txtAlertBanner.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
