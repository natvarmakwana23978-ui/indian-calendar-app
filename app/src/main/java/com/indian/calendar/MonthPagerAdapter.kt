package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonthPagerAdapter(
    private val monthsData: List<List<String>>
) : RecyclerView.Adapter<MonthPagerAdapter.MonthViewHolder>() {

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // અહીં XML માંથી rvCalendar આઈડી લીધું છે
        val rvDays: RecyclerView = view.findViewById(R.id.rvCalendar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_view, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val days = monthsData[position]
        
        // CalendarAdapter ને લિસ્ટ અને ક્લિક ફંક્શન બંને આપ્યા
        val adapter = CalendarAdapter(days) { day: String, pos: Int ->
            // દિવસ પર ક્લિક થાય ત્યારે શું કરવું તે અહીં લખી શકાય
        }
        
        holder.rvDays.layoutManager = GridLayoutManager(holder.itemView.context, 7)
        holder.rvDays.adapter = adapter
    }

    override fun getItemCount(): Int = monthsData.size
}
