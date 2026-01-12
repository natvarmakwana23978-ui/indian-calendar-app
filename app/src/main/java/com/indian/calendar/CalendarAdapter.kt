package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// અહીં ArrayList ને બદલે List<String> વાપર્યું છે
class CalendarAdapter(private val days: List<String>) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        // simple_list_item_1 ને બદલે કસ્ટમ વ્યુ પણ વાપરી શકાય, અત્યારે ટેસ્ટિંગ માટે આ ઠીક છે
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayText.text = days[position]
        holder.dayText.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    override fun getItemCount(): Int = days.size

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(android.R.id.text1)
    }
}
