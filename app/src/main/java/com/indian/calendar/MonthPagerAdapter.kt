package com.indian.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MonthPagerAdapter(
    private val context: Context,
    private val startCalendar: Calendar,
    private val sheetData: Map<String, CalendarDayData>
) : RecyclerView.Adapter<MonthPagerAdapter.MonthViewHolder>() {

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.rvCalendar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_calendar_view, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val calendar = startCalendar.clone() as Calendar
        calendar.add(Calendar.MONTH, position - 500)
        
        val daysInMonth = mutableListOf<String>()
        val tempCal = calendar.clone() as Calendar
        tempCal.set(Calendar.DAY_OF_MONTH, 1)
        
        val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1
        for (i in 0 until firstDayOfWeek) daysInMonth.add("")
        
        val maxDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDays) daysInMonth.add(i.toString())

        val monthYearStr = "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"

        holder.recyclerView.layoutManager = GridLayoutManager(context, 7)
        holder.recyclerView.adapter = CalendarAdapter(daysInMonth, sheetData, monthYearStr)
    }

    override fun getItemCount(): Int = 1000
}
