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
    private val monthDataProvider: (Calendar) -> List<CalendarDayData>
) : RecyclerView.Adapter<MonthPagerAdapter.MonthViewHolder>() {

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.rvCalendar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_calendar_view, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val calendar = startCalendar.clone() as Calendar
        calendar.add(Calendar.MONTH, position - 500)

        val monthData = monthDataProvider(calendar)

        holder.recyclerView.layoutManager = GridLayoutManager(context, 7)
        holder.recyclerView.adapter = CalendarAdapter(monthData)
    }

    override fun getItemCount(): Int = 1000
}
