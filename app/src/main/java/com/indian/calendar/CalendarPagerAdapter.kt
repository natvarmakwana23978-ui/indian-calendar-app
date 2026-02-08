package com.indian.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 12 // ૧૨ મહિના માટે

    override fun createFragment(position: Int): Fragment {
        val fragment = MonthFragment()
        // આ રીતે ડેટા પાસ કરવાથી 'newInstance' ની જરૂર નહીં પડે
        return fragment
    }
}
