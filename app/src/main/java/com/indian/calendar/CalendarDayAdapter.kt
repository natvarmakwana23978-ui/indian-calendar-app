package com.indian.calendar

import android.graphics.Color
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarDayAdapter(private val days: List<CalendarDayData>, private val colIndex: Int) : 
    RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // ખાતરી કરો કે આ ID તમારા item_calendar_day.xml માં છે
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
        val txtAlert: TextView = view.findViewById(R.id.txtAlertBanner)
        val txtTomorrow: TextView = view.findViewById(R.id.txtTomorrowNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // લખાણ દેખાય તે માટે કાળો કલર સેટ કર્યો છે
        holder.txtDate.text = day.Date
        holder.txtDate.setTextColor(Color.BLACK)
        
        holder.txtTithi.text = when(colIndex) { 
            1 -> day.Gujarati 
            2 -> day.Hindi 
            else -> day.Gujarati 
        }
        holder.txtTithi.setTextColor(Color.DKGRAY)

        // લાલ બેનર (રેડ એલર્ટ) - જો ડેટામાં ચેતવણી હોય તો જ બતાવો
        // અત્યારે ટેસ્ટિંગ માટે પહેલી આઇટમમાં બતાવશે
        holder.txtAlert.visibility = if(position == 0) View.VISIBLE else View.GONE
        
        val tomorrow = days.getOrNull(position + 1)
        if (tomorrow != null && tomorrow.Gujarati.contains("બીજ")) {
            holder.txtTomorrow.visibility = View.VISIBLE
            holder.txtTomorrow.text = "આવતીકાલે ${tomorrow.Gujarati} છે, જય રામાપીર"
            holder.txtTomorrow.setTextColor(Color.BLUE)
        } else {
            holder.txtTomorrow.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
