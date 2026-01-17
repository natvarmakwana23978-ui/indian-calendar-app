package com.indian.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.indian.calendar.model.CalendarDayData

class MonthPagerAdapter(
    activity: FragmentActivity,
    private val dayList: List<CalendarDayData>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = dayList.size

    override fun createFragment(position: Int): Fragment {
        // અહીં હવે newInstance ભૂલ નહીં આપે
        return CalendarDayFragment.newInstance(dayList[position])
    }
}
