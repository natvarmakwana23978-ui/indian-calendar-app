package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.indian.calendar.R

// આ લાઈન હોવી ખૂબ જરૂરી છે [cite: 2026-01-23]
class CalendarAdapter(
    private val days: List<CalendarDayData>,
    private val selectedHeader: String
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // ૧. તારીખ બતાવવાનું લોજિક
        val fullDate = day.englishDate
        val dateOnly = if (fullDate.contains("/")) {
            fullDate.substringBefore("/")
        } else {
            fullDate
        }
        holder.tvEng.text = dateOnly

        // ૨. ગૂગલ શીટની વિગત
        val calendarInfo = day.allData.get(selectedHeader)?.asString ?: ""
        holder.tvLoc.text = calendarInfo
        
        // ૩. કલર કોડિંગ (તમારા આઈડિયા મુજબ) [cite: 2026-01-07]
        val rawData = day.allData.toString()
        when {
            rawData.contains("Sunday", ignoreCase = true) || rawData.contains("Holiday", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFEBEE"))
                holder.tvEng.setTextColor(Color.RED)
            }
            rawData.contains("નૂતન વર્ષ", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFF3E0"))
            }
            else -> {
                holder.itemView.setBackgroundColor(Color.WHITE)
                holder.tvEng.setTextColor(Color.BLACK)
            }
        }

        holder.itemView.setOnClickListener {
            showDetailsCard(holder.itemView.context, day, calendarInfo)
        }
    }

    private fun showDetailsCard(context: android.content.Context, day: CalendarDayData, info: String) {
        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_day_details, null)
        
        view.findViewById<TextView>(R.id.tvDetailTitle).text = "તારીખ: ${day.englishDate}"
        view.findViewById<TextView>(R.id.tvDetailInfo).text = info
        
        view.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.setContentView(view)
        dialog.show()
    }

    override fun getItemCount() = days.size
}
