package com.indian.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class MonthFragment : Fragment() {
    companion object {
        fun newInstance(monthData: List<JsonObject>, lang: String): MonthFragment {
            val f = MonthFragment()
            val b = Bundle()
            b.putString("DATA", Gson().toJson(monthData))
            b.putString("LANG", lang)
            f.arguments = b
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_month, container, false)
        val rv = v.findViewById<RecyclerView>(R.id.rvMonthCalendar)
        
        val jsonData = arguments?.getString("DATA")
        val lang = arguments?.getString("LANG") ?: ""
        val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
        
        val finalItems = mutableListOf<CalendarDayData>()

        if (dataList.isNotEmpty()) {
            // શીટમાંથી પહેલા દિવસનો વાર મેળવો (દા.ત. ગુરુવાર)
            val firstDayName = dataList[0].get("DAY")?.asString ?: ""
            
            // વાર મુજબ ઓફસેટ (રવિ=0, સોમ=1, મંગળ=2, બુધ=3, ગુરુ=4, શુક્ર=5, શનિ=6)
            val offset = when {
                firstDayName.contains("રવિ") || firstDayName.lowercase().contains("sun") -> 0
                firstDayName.contains("સોમ") || firstDayName.lowercase().contains("mon") -> 1
                firstDayName.contains("મંગળ") || firstDayName.lowercase().contains("tue") -> 2
                firstDayName.contains("બુધ") || firstDayName.lowercase().contains("wed") -> 3
                firstDayName.contains("ગુરુ") || firstDayName.lowercase().contains("thu") -> 4
                firstDayName.contains("શુક્ર") || firstDayName.lowercase().contains("fri") -> 5
                firstDayName.contains("શનિ") || firstDayName.lowercase().contains("sat") -> 6
                else -> 0
            }

            // ખાલી ખાના ઉમેરો
            for (i in 0 until offset) {
                finalItems.add(CalendarDayData("", JsonObject()))
            }

            // શીટની તારીખો ઉમેરો
            for (item in dataList) {
                val engDateFull = item.get("ENGLISH")?.asString ?: ""
                val dateOnly = if (engDateFull.contains("/")) engDateFull.split("/")[0] else engDateFull
                finalItems.add(CalendarDayData(dateOnly, item))
            }
        }
        
        rv.layoutManager = GridLayoutManager(context, 7)
        rv.adapter = CalendarAdapter(finalItems, lang)
        return v
    }
}
