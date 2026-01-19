package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarSelectionAdapter(
    private val items: List<CalendarItem>,
    private val onClick: (CalendarItem) -> Unit
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // android.R.id.text1 એ સિસ્ટમનું ડિફોલ્ટ ID છે
        val txtName: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // અહીં simple_list_item_1 વાપર્યું છે જે લિસ્ટ બતાવવા માટે સૌથી સરળ છે
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtName.text = item.name
        // આ લાઈન ફરજિયાત ઉમેરવી જેથી સફેદ લખાણની સમસ્યા ન રહે
        holder.txtName.setTextColor(Color.BLACK) 
        holder.txtName.setPadding(32, 48, 32, 48)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
