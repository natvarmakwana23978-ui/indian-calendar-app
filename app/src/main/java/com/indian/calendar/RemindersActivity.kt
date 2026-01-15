package com.indian.calendar

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class UserReminder(val time: String, val note: String)

class RemindersActivity : AppCompatActivity() {

    private lateinit var etNote: EditText
    private lateinit var etTime: EditText
    private lateinit var btnAdd: Button
    private lateinit var lvReminders: ListView

    private val remindersList = mutableListOf<UserReminder>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        etNote = findViewById(R.id.etNote)
        etTime = findViewById(R.id.etTime)
        btnAdd = findViewById(R.id.btnAddReminder)
        lvReminders = findViewById(R.id.lvReminders)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lvReminders.adapter = adapter

        loadReminders()
        refreshList()

        btnAdd.setOnClickListener {
            val note = etNote.text.toString()
            val time = etTime.text.toString()

            if (note.isNotEmpty() && time.isNotEmpty()) {
                remindersList.add(UserReminder(time, note))
                saveReminders()
                refreshList()
            } else {
                Toast.makeText(this, "Enter time and note", Toast.LENGTH_SHORT).show()
            }
        }

        lvReminders.setOnItemLongClickListener { _, _, position, _ ->
            remindersList.removeAt(position)
            saveReminders()
            refreshList()
            true
        }
    }

    private fun refreshList() {
        adapter.clear()
        adapter.addAll(remindersList.map { "${it.time} - ${it.note}" })
    }

    private fun saveReminders() {
        val prefs = getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val set = remindersList.map { "${it.time}|${it.note}" }.toSet()
        prefs.edit().putStringSet("user_reminders", set).apply()
    }

    private fun loadReminders() {
        val prefs = getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("user_reminders", emptySet()) ?: emptySet()
        remindersList.clear()
        set.forEach {
            val p = it.split("|")
            if (p.size == 2) remindersList.add(UserReminder(p[0], p[1]))
        }
    }
}
