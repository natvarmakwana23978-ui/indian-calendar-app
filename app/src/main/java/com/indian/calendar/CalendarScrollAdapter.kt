package com.indian.calendar

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

    // ૧૨ મહિનાના નામ (અંગ્રેજીમાં ટાઈટલ માટે)
    private val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(position, months[position], jsonData, selectedLang)
    }

    override fun getItemCount(): Int = 12

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtEngMonth: TextView = view.findViewById(R.id.txtEngMonth)
        private val daysRecyclerView: RecyclerView = view.findViewById(R.id.daysRecyclerView)

        fun bind(monthPos: Int, monthName: String, jsonData: String, lang: String) {
            txtEngMonth.text = "$monthName 2026"

            val dayList = mutableListOf<DayModel>()

            try {
                val fullData = JSONArray(jsonData)
                
                // શીટના ડેટામાંથી આ મહિનાની તારીખો ફિલ્ટર કરવી
                for (i in 0 until fullData.length()) {
                    val obj = fullData.getJSONObject(i)
                    
                    // જો તમારી સ્ક્રિપ્ટ 'month' મોકલતી હોય તો આ ફિલ્ટર કામ કરશે
                    // અત્યારે આપણે સીધો ડેટા મેપ કરીએ છીએ
                    val date = obj.optString("date", "")
                    val tithi = obj.optString("tithi", "")

                    if (date.isNotEmpty()) {
                        dayList.add(DayModel(date, tithi))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // જો શીટનો ડેટા ન મળે તો ખાલી ખાના ન દેખાય એટલે ૧ થી ૩૦ તારીખ બતાવી દેશે
            if (dayList.isEmpty()) {
                for (i in 1..30) {
                    dayList.add(DayModel(i.toString(), "તિથિ $i"))
                }
            }

            // મહિનાની તારીખોની ગ્રીડ (૭ કોલમ: રવિ થી શનિ)
            daysRecyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
            daysRecyclerView.adapter = DaysAdapter(dayList)
            
            // સ્મૂધ સ્ક્રોલિંગ માટે આ જરૂરી છે
            daysRecyclerView.isNestedScrollingEnabled = false
        }
    }
}
