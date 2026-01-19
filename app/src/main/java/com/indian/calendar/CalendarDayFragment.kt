package com.indian.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
// model ઈમ્પોર્ટ હટાવી દીધો છે કારણ કે બધી ફાઈલો એક જ પેકેજમાં છે

class CalendarDayFragment : Fragment() {

    companion object {
        fun newInstance(dayData: CalendarDayData): CalendarDayFragment {
            val fragment = CalendarDayFragment()
            val args = Bundle()
            args.putParcelable("day_data", dayData)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_day, container, false)
        val dayData = arguments?.getParcelable<CalendarDayData>("day_data")

        // તમારી XML ફાઈલ મુજબના સાચા ID
        val txtLine1 = view.findViewById<TextView>(R.id.line1)
        val txtLine2 = view.findViewById<TextView>(R.id.line2)
        val txtLine3 = view.findViewById<TextView>(R.id.line3)

        dayData?.let {
            txtLine1?.text = it.Date      // ENGLISH તારીખ line1 માં
            txtLine2?.text = it.Gujarati  // ગુજરાતી વિગત line2 માં
            txtLine3?.text = ""           // અત્યારે line3 ખાલી
        }
        return view
    }
}
