package com.indian.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.JsonObject

class CalendarPagerAdapter(
    fa: FragmentActivity,
    private val allData: List<JsonObject>,
    private val lang: String
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 12

    override fun createFragment(position: Int): Fragment {
        val monthNum = position + 1
        val monthData = allData.filter { 
            val engDate = it.get("ENGLISH")?.asString ?: ""
            val parts = engDate.split("/")
            parts.size >= 2 && parts[1].toIntOrNull() == monthNum
        }
        return MonthFragment.newInstance(monthData, lang)
    }
}
