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
import java.util.*

class MonthFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_month, container, false)
        val rv = v.findViewById<RecyclerView>(R.id.rvMonthCalendar)
        
        val jsonData = arguments?.getString("DATA") ?: ""
        val lang = arguments?.getString("LANG") ?: "ગુજરાતી (Gujarati)"
        val calIndex = arguments?.getInt("CAL_INDEX") ?: 0
        val monthNum = arguments?.getInt("MONTH_NUM") ?: 1
        
        val gson = Gson()
        val rootObject = gson.fromJson(jsonData, JsonObject::class.java)
        val allCalendars = rootObject.getAsJsonArray("allCalendars")
        
        // ૧. પસંદ કરેલું કેલેન્ડર લો
        val fullYearData: List<JsonObject> = gson.fromJson(
            allCalendars[calIndex], object : TypeToken<List<JsonObject>>() {}.type
        )

        // ૨. માત્ર ચાલુ મહિનાનો ડેટા ફિલ્ટર કરો
        val currentMonthData = fullYearData.filter {
            val engDate = it.get("ENGLISH")?.asString ?: ""
            val parts = engDate.split("/")
            parts.size >= 2 && parts[1].toIntOrNull() == monthNum
        }

        val finalItems = mutableListOf<CalendarDayData>()

        if (currentMonthData.isNotEmpty()) {
            val firstDateStr = currentMonthData[0].get("ENGLISH")?.asString ?: ""
            val parts = firstDateStr.split("/")
            if (parts.size == 3) {
                val cal = Calendar.getInstance()
                cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
                val offset = cal.get(Calendar.DAY_OF_WEEK) - 1

                for (i in 0 until offset) {
                    finalItems.add(CalendarDayData("", JsonObject(), 0))
                }

                for (i in currentMonthData.indices) {
                    val item = currentMonthData[i]
                    val engDateFull = item.get("ENGLISH")?.asString ?: ""
                    val dateOnly = if (engDateFull.contains("/")) engDateFull.split("/")[0] else engDateFull
                    
                    val isSunday = (offset + i) % 7 == 0
                    val color = if (isSunday) 1 else 0

                    finalItems.add(CalendarDayData(dateOnly, item, color))
                }
            }
        }
        
        rv.layoutManager = GridLayoutManager(context, 7)
        rv.adapter = CalendarAdapter(finalItems, lang)
        return v
    }

    companion object {
        fun newInstance(fullJson: String, lang: String, calIndex: Int, monthNum: Int): MonthFragment {
            val f = MonthFragment()
            val b = Bundle()
            b.putString("DATA", fullJson)
            b.putString("LANG", lang)
            b.putInt("CAL_INDEX", calIndex)
            b.putInt("MONTH_NUM", monthNum)
            f.arguments = b
            return f
        }
    }
}
