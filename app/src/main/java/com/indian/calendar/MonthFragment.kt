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
        
        val gson = Gson()
        val rootObject = gson.fromJson(jsonData, JsonObject::class.java)
        val allCalendars = rootObject.getAsJsonArray("allCalendars")
        
        // ૨૭ માંથી પસંદ કરેલું કેલેન્ડર
        val currentCalendarData: List<JsonObject> = gson.fromJson(
            allCalendars[calIndex], object : TypeToken<List<JsonObject>>() {}.type
        )

        val finalItems = mutableListOf<CalendarDayData>()

        if (currentCalendarData.isNotEmpty()) {
            // ૧. તારીખના આધારે રવિવાર ક્યારે છે તે નક્કી કરવા માટેની ગણતરી
            val firstDateStr = currentCalendarData[0].get("ENGLISH")?.asString ?: ""
            val parts = firstDateStr.split("/")
            if (parts.size == 3) {
                val cal = Calendar.getInstance()
                cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
                val offset = cal.get(Calendar.DAY_OF_WEEK) - 1 // Sunday=0, Monday=1...

                // શરૂઆતના ખાલી ખાના (જેમાં ડેટા નથી)
                for (i in 0 until offset) {
                    finalItems.add(CalendarDayData("", JsonObject(), 0))
                }

                // ૨. તારીખો ઉમેરવી અને રવિવાર માટે લાલ રંગ સેટ કરવો
                for (i in 0 until currentCalendarData.size()) {
                    val item = currentCalendarData[i]
                    val engDateFull = item.get("ENGLISH")?.asString ?: ""
                    val dateOnly = if (engDateFull.contains("/")) engDateFull.split("/")[0] else engDateFull
                    
                    // રવિવાર નક્કી કરવા માટેનું લોજિક: (offset + current_index) % 7 == 0
                    val isSunday = (offset + i) % 7 == 0
                    val color = if (isSunday) 1 else 0 // ૧ એટલે લાલ (તમારા colorCode મુજબ)

                    finalItems.add(CalendarDayData(dateOnly, item, color))
                }
            }
        }
        
        rv.layoutManager = GridLayoutManager(context, 7)
        // અહીં CalendarAdapter માં માત્ર finalItems અને lang મોકલીએ છીએ
        rv.adapter = CalendarAdapter(finalItems, lang)
        return v
    }

    companion object {
        fun newInstance(fullJson: String, lang: String, calIndex: Int): MonthFragment {
            val f = MonthFragment()
            val b = Bundle()
            b.putString("DATA", fullJson)
            b.putString("LANG", lang)
            b.putInt("CAL_INDEX", calIndex)
            f.arguments = b
            return f
        }
    }
}
