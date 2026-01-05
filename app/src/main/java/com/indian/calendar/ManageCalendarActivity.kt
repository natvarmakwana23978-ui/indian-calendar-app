package com.indian.calendar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.util.*

class ManageCalendarActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: CalendarListAdapter
    private var fullList = mutableListOf<Pair<String, String?>>()
    private lateinit var searchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_calendar)

        db = AppDatabase.getDatabase(this)
        searchBar = findViewById(R.id.searchDate)
        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadData()

        // સર્ચ લોજિક
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val filtered = fullList.filter { it.first.contains(s.toString()) }
                adapter.updateList(filtered)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun loadData() {
        lifecycleScope.launch {
            val savedNotes = db.userNoteDao().getAllNotes().associateBy({ it.dateKey }, { it.personalNote })
            fullList.clear()
            
            // ૩૬૫ દિવસ જનરેટ કરો
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            
            for (i in 0..364) {
                val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
                val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                val dateKey = "$day/$month"
                fullList.add(dateKey to savedNotes[dateKey])
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            adapter = CalendarListAdapter(fullList) { dateKey ->
                showEditDialog(dateKey)
            }
            findViewById<RecyclerView>(R.id.calendarRecyclerView).adapter = adapter
        }
    }

    private fun showEditDialog(dateKey: String) {
        val input = EditText(this)
        input.hint = "અહીં તમારી નોંધ લખો..."
        
        AlertDialog.Builder(this)
            .setTitle("$dateKey માટેની નોંધ")
            .setView(input)
            .setPositiveButton("સેવ કરો") { _, _ ->
                val noteText = input.text.toString()
                lifecycleScope.launch {
                    db.userNoteDao().insertNote(UserNote(dateKey = dateKey, personalNote = noteText))
                    Toast.makeText(this@ManageCalendarActivity, "નોંધ સાચવવામાં આવી!", Toast.LENGTH_SHORT).show()
                    loadData() // લિસ્ટ રિફ્રેશ કરો
                }
            }
            .setNegativeButton("કેન્સલ", null)
            .show()
    }
}
