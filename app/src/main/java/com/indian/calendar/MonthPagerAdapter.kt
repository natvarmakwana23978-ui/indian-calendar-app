package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonthPagerAdapter(private val allMonthsData: List<List<String>>) : 
    RecyclerView.Adapter<MonthPagerAdapter.MonthViewHolder>() {

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.monthRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.month_page_layout, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        // દરેક પેજ માટે 7 કોલમની ગ્રીડ સેટ કરવી
        holder.recyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 7)
        holder.recyclerView.adapter = CalendarAdapter(allMonthsData[position])
    }

    override fun getItemCount(): Int = allMonthsData.size
}

