package com.indian.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.indian.calendar.model.CalendarDayData

class CalendarDayFragment : Fragment() {

    private lateinit var dayData: CalendarDayData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayData = requireArguments().getParcelable(ARG_DATA)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_calendar_day, container, false)

        view.findViewById<TextView>(R.id.line1).text =
            dayData.Date

        view.findViewById<TextView>(R.id.line2).text =
            "${dayData.Gujarati_Month} ${dayData.Tithi}, ${dayData.Day}"

        view.findViewById<TextView>(R.id.line3).text =
            dayData.Festival_English

        return view
    }

    companion object {
        private const val ARG_DATA = "calendar_data"

        fun newInstance(data: CalendarDayData): CalendarDayFragment {
            val fragment = CalendarDayFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ARG_DATA, data)
            }
            return fragment
        }
    }
}

