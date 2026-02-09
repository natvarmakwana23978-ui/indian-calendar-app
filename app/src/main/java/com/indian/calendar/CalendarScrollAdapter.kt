package com.smart.reminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class CalendarScrollAdapter(
    private val jsonData: String,
    private val selectedLang: String
) : RecyclerView.Adapter<CalendarScrollAdapter.MonthViewHolder>() {

    private val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(position, months[position], jsonData)
    }

    override fun getItemCount(): Int = 12

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtEngMonth: TextView = view.findViewById(R.id.txtEngMonth)
        private val daysRecyclerView: RecyclerView = view.findViewById(R.id.daysRecyclerView)

        fun bind(monthPos: Int, monthName: String, jsonData: String) {
            txtEngMonth.text = "$monthName 2026"

            val dayList = mutableListOf<DayModel>()

            try {
                if (jsonData.isNotEmpty()) {
                    val fullData = JSONArray(jsonData)
                    
                    for (i in 0 until fullData.length()) {
                        val obj = fullData.getJSONObject(i)
                        // તમારી લિંક મુજબ ડેટા "data" કી માં છે
                        val rawLine = obj.optString("data", "")

                        if (rawLine.contains("|")) {
                            val parts = rawLine.split("|").map { it.trim() }
                            
                            // ઇન્ડેક્સ ચેક: તમારી લાઈનમાં 1 નંબરે તારીખ અને 6 નંબરે તિથિ છે
                            if (parts.size > 6) {
                                val dateStr = parts[1] // તારીખ (દા.ત. 1)
                                val tithiStr = parts[6] // તિથિ (દા.ત. એકમ)
                                
                                if (dateStr.isNotEmpty()) {
                                    dayList.add(DayModel(dateStr, tithiStr))
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // જો કોઈ કારણસર ડેટા ન મળે તો ખાલી ૧ થી ૩૦ તારીખ બતાવશે (ક્રેશ નહીં થાય)
            if (dayList.isEmpty()) {
                for (i in 1..30) {
                    dayList.add(DayModel(i.toString(), ""))
                }
            }

            // ગ્રીડ સેટઅપ
            daysRecyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
            daysRecyclerView.adapter = DaysAdapter(dayList)
            daysRecyclerView.isNestedScrollingEnabled = false
        }
    }
}
