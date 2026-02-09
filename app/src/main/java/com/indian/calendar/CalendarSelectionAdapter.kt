package com.smart.reminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarSelectionAdapter(
    private val languages: List<String>,
    private val onItemClick: (String) -> Unit // આ લાઈન હોવી જરૂરી છે
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvLang: TextView = v.findViewById(android.R.id.text1) // અથવા તમારું Custom ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lang = languages[position]
        holder.tvLang.text = lang
        holder.itemView.setOnClickListener { onItemClick(lang) }
    }

    override fun getItemCount() = languages.size
}
