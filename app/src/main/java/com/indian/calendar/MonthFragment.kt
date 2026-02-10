package com.smart.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.smart.reminder.databinding.FragmentMonthBinding

class MonthFragment : Fragment() {

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // ગ્રીડ લેઆઉટ (૭ દિવસ માટે)
        binding.recyclerViewMonth.layoutManager = GridLayoutManager(context, 7)
        
        // અહીં તમારા ડેટા લોડ કરવાનું લોજિક આવશે
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
