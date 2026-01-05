package com.indian.calendar

import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_calendar)

        val rv = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        rv.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ManageCalendarActivity)
            val savedNotes = db.userNoteDao().getAllNotes().associateBy { it.dateKey }
            
            val days = mutableListOf<Pair<String, String?>>()
            val cal = Calendar.getInstance()
            cal.set(2026, 0, 1)
            for (i in 0..364) {
                val key = SimpleDateFormat("dd/MM", Locale.getDefault()).format(cal.time)
                days.add(key to savedNotes[key]?.personalNote)
                cal.add(Calendar.DAY_OF_YEAR, 1)
            }

            rv.adapter = CalendarListAdapter(days) { dateKey ->
                val input = EditText(this@ManageCalendarActivity)
                AlertDialog.Builder(this@ManageCalendarActivity).setTitle(dateKey).setView(input)
                    .setPositiveButton("Save") { _, _ ->
                        lifecycleScope.launch {
                            db.userNoteDao().insertNote(UserNote(dateKey, input.text.toString()))
                            Toast.makeText(this@ManageCalendarActivity, "Saved", Toast.LENGTH_SHORT).show()
                            recreate() 
                        }
                    }.show()
            }
        }
    }
}
