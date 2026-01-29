// onBindViewHolder માં DayViewHolder માટેનો ફેરફાર:
if (holder is DayViewHolder) {
    if (item == "EMPTY_SLOT") {
        holder.itemView.visibility = View.INVISIBLE
        return
    }
    holder.itemView.visibility = View.VISIBLE
    val day = item as CalendarDayData
    
    val dateParts = day.englishDate.split("/")
    val dayInSheet = day.allData.get("Day")?.asString ?: ""
    val festival = day.allData.get("Name of Festival")?.asString ?: ""
    val localInfo = day.allData.get(selectedLang)?.asString ?: ""

    // ૧. વાર સેટ કરો (નાના અક્ષરે ઉપર)
    holder.tvDayLabel.text = dayInSheet

    // ૨. અંગ્રેજી તારીખ (મોટી વચ્ચે)
    holder.tvDate.text = dateParts[0]

    // ૩. લોકલ વિગત (જો તહેવાર હોય તો તહેવાર, નહીંતર તિથિ)
    holder.tvLocal.text = if (festival.isNotEmpty()) festival else localInfo

    // રંગોનું લોજિક (રવિવાર/શનિવાર અને તહેવાર મુજબ)
    val isSun = dayInSheet.contains("Sun")
    val d = dateParts[0].toInt()
    val isRedSat = dayInSheet.contains("Sat") && ((d in 8..14) || (d in 22..28))

    if (isSun || isRedSat) {
        holder.itemView.setBackgroundColor(Color.RED)
        setTextColorWhite(holder)
    } else if (festival.isNotEmpty()) {
        holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00")) // Orange
        setTextColorWhite(holder)
    } else {
        holder.itemView.setBackgroundColor(Color.WHITE)
        holder.tvDate.setTextColor(Color.BLACK)
        holder.tvDayLabel.setTextColor(Color.GRAY)
        holder.tvLocal.setTextColor(Color.DKGRAY)
    }
}

// હેલ્પર ફંક્શન
private fun setTextColorWhite(holder: DayViewHolder) {
    holder.tvDate.setTextColor(Color.WHITE)
    holder.tvDayLabel.setTextColor(Color.WHITE)
    holder.tvLocal.setTextColor(Color.WHITE)
}
