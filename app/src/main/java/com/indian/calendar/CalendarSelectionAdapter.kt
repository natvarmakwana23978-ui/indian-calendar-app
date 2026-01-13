package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarSelectionAdapter(
    private val list: List<CalendarModel>,
    private val onItemClick: (CalendarModel, Int) -> Unit
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.textView.text = item.name
        holder.itemView.setOnClickListener { onItemClick(item, position) }
    }

    override fun getItemCount() = list.size
}
