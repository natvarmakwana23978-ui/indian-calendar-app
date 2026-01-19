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
        // android.R.id.text1 એ simple_list_item_1 લેઆઉટની ડિફોલ્ટ ID છે [cite: 2026-01-14]
        val txtName: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // લિસ્ટ બતાવવા માટે સિસ્ટમનું ડિફોલ્ટ લેઆઉટ વાપર્યું છે [cite: 2026-01-14]
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        
        // જો નામ (name) ન મળે, તો 'Calendar' સાથે તેનું ID બતાવશે [cite: 2026-01-14]
        holder.txtName.text = item.name ?: "Calendar ${item.id ?: (position + 1)}"
        
        // અક્ષરોનો કલર ચોક્કસપણે કાળો રાખવો જેથી સફેદ બેકગ્રાઉન્ડ પર દેખાય [cite: 2026-01-14]
        holder.txtName.setTextColor(Color.BLACK)
        holder.txtName.textSize = 20f
        holder.txtName.setPadding(40, 50, 40, 50)
        
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
