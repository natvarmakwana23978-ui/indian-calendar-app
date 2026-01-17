package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarListAdapter(
    private val items: List<CalendarItem>,
    private val onItemClick: (CalendarItem) -> Unit
) : RecyclerView.Adapter<CalendarListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // અહીં android.R.id.text1 એ સિસ્ટમનું ડિફોલ્ટ આઈડી છે
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // આપણે CalendarItem માં 'calendarName' રાખ્યું છે એટલે અહીં એ જ વાપરવું
        holder.textView.text = item.calendarName
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size
}
