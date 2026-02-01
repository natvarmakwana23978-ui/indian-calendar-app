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
        
        val finalItems = dataList.map { CalendarDayData(it.get("ENGLISH").asString, it) }

        rv.layoutManager = GridLayoutManager(context, 7)
        rv.adapter = CalendarAdapter(finalItems, lang)
        return v
    }
}
