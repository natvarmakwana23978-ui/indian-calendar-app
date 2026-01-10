package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ડેટા મોડેલ - જે ગૂગલ શીટના ડેટાને સાચવશે
data class CalendarModel(val name: String, val creator: String)

class CalendarSelectionAdapter(
    private val list: List<CalendarModel>,
    private val onClick: (CalendarModel) -> Unit
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    // વ્યુ હોલ્ડર: જે લિસ્ટની એક રો (Row) ની ડિઝાઇન સાચવશે
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtCalendarName: TextView = view.findViewById(android.R.id.text1)
        val txtCreatorName: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // એન્ડ્રોઇડનું ડિફોલ્ટ બે લાઇન વાળું લેઆઉટ વાપર્યું છે
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        
        // કેલેન્ડરનું નામ (Column B માંથી આવશે)
        holder.txtCalendarName.text = item.name
        holder.txtCalendarName.textSize = 18f
        holder.txtCalendarName.setPadding(0, 10, 0, 5)

        // બનાવનાર અથવા વિગત (Column C માંથી આવશે)
        holder.txtCreatorName.text = "Details: ${item.creator}"
        holder.txtCreatorName.alpha = 0.7f // થોડું આછું દેખાય તે માટે

        // જ્યારે કોઈ કેલેન્ડર પર ક્લિક થાય ત્યારે
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = list.size
}
