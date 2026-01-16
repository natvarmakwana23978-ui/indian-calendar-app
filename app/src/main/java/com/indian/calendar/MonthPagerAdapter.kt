package com.indian.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.indian.calendar.model.CalendarDayData

class MonthPagerAdapter(
    activity: FragmentActivity,
    private val data: List<CalendarDayData>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment {
        return CalendarDayFragment.newInstance(data[position])
    }
}
