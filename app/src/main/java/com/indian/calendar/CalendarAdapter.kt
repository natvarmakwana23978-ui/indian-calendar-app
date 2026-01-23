override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val day = days[position]
    
    // ૧. તારીખ બતાવવાનું સાચું લોજિક
    val fullDate = day.englishDate // દા.ત. "1/1/2026"
    val dateOnly = if (fullDate.contains("/")) {
        fullDate.substringBefore("/") // આ "1" આપશે
    } else {
        fullDate
    }
    holder.tvEng.text = dateOnly

    // ૨. પસંદ કરેલા કેલેન્ડરની વિગત (દા.ત. પોષ સુદ-૧૩)
    val calendarInfo = day.allData.get(selectedHeader)?.asString ?: ""
    holder.tvLoc.text = calendarInfo
    
    // ૩. કલર કોડિંગ (તમારા આઈડિયા મુજબ)
    val rawData = day.allData.toString()
    when {
        rawData.contains("Sunday", ignoreCase = true) || rawData.contains("Holiday", ignoreCase = true) -> {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFEBEE")) // લાલ
            holder.tvEng.setTextColor(Color.RED)
        }
        rawData.contains("નૂતન વર્ષ", ignoreCase = true) -> {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFF3E0")) // કેસરી
        }
        else -> {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.tvEng.setTextColor(Color.BLACK)
        }
    }

    holder.itemView.setOnClickListener {
        showDetailsCard(holder.itemView.context, day, calendarInfo)
    }
}
