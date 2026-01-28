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

    private var lastMonth = ""
    private var saturdayCounter = 0

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
            
            if (title == "EMPTY") {
                tv.text = ""
                tv.setBackgroundColor(Color.TRANSPARENT)
            } else {
                tv.text = title
                tv.gravity = Gravity.CENTER
                tv.setBackgroundColor(Color.parseColor("#EEEEEE"))
                tv.setTextColor(Color.BLACK)
                tv.setTypeface(null, Typeface.BOLD) // અહીં સુધારો કર્યો છે
            }
        } else if (holder is DayViewHolder) {
            val item = items[position]
            if (item == "EMPTY") {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                holder.tvDate.text = ""
                holder.tvLocal.text = ""
                return
            }
            
            val day = item as CalendarDayData
            holder.tvDate.text = day.englishDate.substringBefore("/")
            val localInfo = day.allData.get(selectedLang)?.asString ?: ""
            holder.tvLocal.text = localInfo

            // વાર નક્કી કરો (જાન્યુઆરી ૨૦૨૬ મુજબ ૦ એટલે રવિવાર, ૬ એટલે શનિવાર)
            val isSunday = position % 7 == 0
            val isSaturday = position % 7 == 6
            
            // શનિવાર ગણવા માટેનું લોજિક
            val currentMonth = day.englishDate.split("/")[1]
            if (currentMonth != lastMonth) {
                lastMonth = currentMonth
                saturdayCounter = 0
            }
            
            var isRedSaturday = false
            if (isSaturday) {
                saturdayCounter++
                if (saturdayCounter == 2 || saturdayCounter == 4) {
                    isRedSaturday = true
                }
            }

            when {
                // રવિવાર અથવા બીજો/ચોથો શનિવાર: લાલ બેકગ્રાઉન્ડ
                isSunday || isRedSaturday -> {
                    holder.itemView.setBackgroundColor(Color.RED)
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
                // તહેવાર (જો વિગત મોટી હોય): કેસરી બેકગ્રાઉન્ડ
                localInfo.length > 15 || (localInfo.contains(" ") && !localInfo.contains("સુદ") && !localInfo.contains("વદ")) -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00"))
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
                // સામાન્ય દિવસ
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
