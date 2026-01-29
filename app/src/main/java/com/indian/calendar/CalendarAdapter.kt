package com.indian.calendar

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val items: List<Any>, private val selectedLang: String) : 
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = if (items[position] is String) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            HeaderViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
            DayViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            val title = items[position] as String
            val tv = holder.itemView.findViewById<TextView>(android.R.id.text1)
            tv.text = if (title == "EMPTY_SLOT") "" else title
            tv.gravity = Gravity.CENTER
            tv.setBackgroundColor(if (title == "EMPTY_SLOT") Color.TRANSPARENT else Color.parseColor("#EEEEEE"))
            tv.setTypeface(null, Typeface.BOLD)
        } else if (holder is DayViewHolder) {
            val item = items[position]
            if (item == "EMPTY_SLOT") {
                holder.itemView.visibility = View.INVISIBLE
                return
            }
            holder.itemView.visibility = View.VISIBLE
            
            val day = item as CalendarDayData
            val dateNum = day.englishDate.split("/")[0]
            holder.tvDate.text = dateNum
            
            val localInfo = day.allData.get(selectedLang)?.asString ?: ""
            holder.tvLocal.text = localInfo

            // પોઝિશન પરથી વાર નક્કી ન કરતા, ડેટા પરથી નક્કી કરો (વધારે સચોટ)
            val isSunday = day.allData.get("ENGLISH").asString.contains("Sun")
            val isSaturday = day.allData.get("ENGLISH").asString.contains("Sat")
            
            // શનિવાર ગણવા માટેનું લોજિક (તારીખ ૮-૧૪ અને ૨૨-૨૮ વચ્ચેના શનિવાર લાલ)
            val d = dateNum.toInt()
            val isSecondOrFourthSat = isSaturday && ((d in 8..14) || (d in 22..28))

            // તહેવાર લોજિક: જો લખાણમાં સુદ/વદ/વાર સિવાય વધારાના શબ્દો હોય
            val cleanInfo = localInfo.replace("સુદ", "").replace("વદ", "").replace("વાર", "").trim()
            val hasFestival = cleanInfo.length > 5 

            when {
                isSunday || isSecondOrFourthSat -> {
                    holder.itemView.setBackgroundColor(Color.RED)
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
                hasFestival -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00"))
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
                else -> {
                    holder.itemView.setBackgroundColor(Color.WHITE)
                    holder.tvDate.setTextColor(Color.BLACK)
                    holder.tvLocal.setTextColor(Color.GRAY)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
    class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocal: TextView = v.findViewById(R.id.tvLocalDate)
    }
}
