package com.indian.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
// 'model' ઈમ્પોર્ટ હટાવી દીધો છે કારણ કે હવે બધું એક જ પેકેજમાં છે

class MonthPagerAdapter(
    activity: FragmentActivity,
    private val dayList: List<CalendarDayData>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = dayList.size

    override fun createFragment(position: Int): Fragment {
        // હવે CalendarDayFragment અને CalendarDayData બંને એક જ પેકેજમાં હોવાથી
        // અહીં કોઈ 'Unresolved reference' એરર આવશે નહીં.
        return CalendarDayFragment.newInstance(dayList[position])
    }
}
