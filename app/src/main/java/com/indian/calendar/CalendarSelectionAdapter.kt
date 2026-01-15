package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData
import kotlinx.parcelize.Parcelize

class CalendarSelectionAdapter(
    private val items: List<CalendarDayData>
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Bind data: use festival/reminder
        // Example:
        // holder.textView.text = item.festival ?: item.reminder ?: ""
    }

    override fun getItemCount(): Int = items.size
}
