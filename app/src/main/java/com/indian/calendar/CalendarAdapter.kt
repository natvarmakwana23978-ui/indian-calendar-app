override fun onBindViewHolder(h: ViewHolder, pos: Int) {
    val d = days[pos]
    if (d.englishDate.isNullOrEmpty()) {
        h.itemView.visibility = View.INVISIBLE
        return
    }
    h.itemView.visibility = View.VISIBLE
    h.eng.text = d.englishDate.split(" ")[2]
    h.loc.text = d.localDate ?: ""

    // ખોટો ડેટા ફિલ્ટર કરવા માટે [cite: 2026-01-21]
    val filterWords = listOf("Tevet", "Shevat", "Adar", "Rajab", "Shaban")
    val alertText = d.alert ?: ""
    
    val containsInvalidWord = filterWords.any { alertText.contains(it, ignoreCase = true) }

    if (alertText.isNotEmpty() && !containsInvalidWord) {
        h.alert.visibility = View.VISIBLE
        h.alert.text = alertText
    } else {
        h.alert.visibility = View.GONE
    }
}
