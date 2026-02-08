package com.indian.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.indian.calendar.databinding.FragmentMonthBinding

class MonthFragment : Fragment() {

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    
    private var monthData: List<CalendarDayData> = emptyList()
    private var language: String = "Gujarati"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // તમારી XML માં ID 'rvMonthCalendar' છે એમ માનીને:
        binding.rvMonthCalendar.layoutManager = GridLayoutManager(context, 7)
        binding.rvMonthCalendar.adapter = MonthAdapter(monthData, language)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
