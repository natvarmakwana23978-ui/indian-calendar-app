package com.indian.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CalendarListAdapter(
    context: Context,
    private val calendarList: List<CalendarItem>
) : ArrayAdapter<CalendarItem>(context, 0, calendarList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val calendarItem = calendarList[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = calendarItem.calendarName

        return view
    }
}
