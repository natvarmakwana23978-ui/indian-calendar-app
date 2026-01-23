package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.indian.calendar.R

class MonthAdapter(
    private val monthData: List<JsonObject>,
    private val selectedHeader: String
) : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // અહીં તમારા સ્ક્રીનશોટ મુજબનું સાચું ID 'calendarRecyclerView' છે [cite: 2026-01-23]
        val rvDays: RecyclerView? = v.findViewById(R.id.calendarRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // અહીં તમારી ફાઈલનું સાચું નામ 'activity_calendar_view' વાપરવું [cite: 2026-01-23]
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_calendar_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daysList = monthData.map { 
            CalendarDayData(it.get("ENGLISH")?.asString ?: "", it) 
        }
        
        holder.rvDays?.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = CalendarAdapter(daysList, selectedHeader)
        }
    }

    override fun getItemCount() = 1
}
