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
        // અહીં તમારા XML મુજબ 'calendarRecyclerView' વાપર્યું છે
        val rvDays: RecyclerView? = v.findViewById(R.id.calendarRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // અહીં એક્ટિવિટીનું નામ 'activity_calendar_view' વાપર્યું છે કારણ કે તમે આ XML આપ્યું છે
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_calendar_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ડેટાને CalendarDayData મોડેલમાં કન્વર્ટ કરો
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
