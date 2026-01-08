package com.indian.calendar

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RemindersActivity : AppCompatActivity() {

    private lateinit var adapter: ReminderAdapter
    private val taskList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        val editTask = findViewById<EditText>(R.id.editTask)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val recyclerView = findViewById<RecyclerView>(R.id.reminderRecyclerView)

        // એડેપ્ટર સેટઅપ કરવું (ડિલીટ ફંક્શન સાથે)
        adapter = ReminderAdapter(taskList) { position ->
            taskList.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "કામ કાઢી નાખ્યું", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 'ઉમેરો' બટન પર ક્લિક કરતા શું થશે?
        btnAdd.setOnClickListener {
            val taskName = editTask.text.toString().trim()
            if (taskName.isNotEmpty()) {
                taskList.add(taskName)
                adapter.notifyDataSetChanged()
                editTask.text.clear() // ટાઈપ કરેલું લખાણ સાફ કરવું
            } else {
                Toast.makeText(this, "કંઈક લખો...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

