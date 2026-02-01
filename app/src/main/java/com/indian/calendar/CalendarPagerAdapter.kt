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
        // મહિના મુજબ ફિલ્ટર (1/ થી 12/ સુધી)
        val monthPrefix = "${position + 1}/"
        val monthData = allData.filter { 
            it.get("ENGLISH").asString.startsWith(monthPrefix) 
        }
        return MonthFragment.newInstance(monthData, lang)
    }
}

