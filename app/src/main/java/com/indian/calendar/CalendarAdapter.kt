package indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

class CalendarAdapter(
    private val days: List<CalendarDayData>,
    private val selectedHeader: String // યુઝરે પસંદ કરેલ કોઈપણ કેલેન્ડરનું હેડર [cite: 2026-01-07]
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate) // આ તિથિ/તારીખ બતાવશે
        val tvAlert: TextView = v.findViewById(R.id.tvAlert) // આ તહેવાર/નોંધ બતાવશે
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayData = days[position]
        
        if (dayData.englishDate.isEmpty()) {
            holder.itemView.visibility = View.INVISIBLE
            return
        }
        holder.itemView.visibility = View.VISIBLE
        
        // ૧. અંગ્રેજી તારીખ સેટ કરો
        val dateParts = dayData.englishDate.split("/")
        holder.tvEng.text = if (dateParts.isNotEmpty()) dateParts[0] else ""
        
        // ૨. ડાયનેમિક ડેટા ડિસ્પ્લે: યુઝરે પસંદ કરેલ કેલેન્ડર મુજબ [cite: 2026-01-07]
        // આ 'selectedHeader' તમારી ગૂગલ શીટની કોઈપણ કોલમ હોઈ શકે છે.
        val calendarElement = dayData.allData.get(selectedHeader)
        val calendarInfo = if (calendarElement != null && !calendarElement.isJsonNull) calendarElement.asString else ""

        if (calendarInfo.isNotEmpty() && calendarInfo != "null") {
            // અહીં કેલેન્ડરની મુખ્ય વિગત (જેમ કે તિથિ કે તારીખ) દેખાશે [cite: 2026-01-22]
            holder.tvLoc.text = calendarInfo
        } else {
            holder.tvLoc.text = ""
        }

        // નોંધ: જો તમારી શીટમાં તહેવારો માટે અલગ કોલમ હોય, 
        // તો આપણે બીજું એક હેડર પણ પાસ કરી શકીએ.
        holder.tvAlert.visibility = View.GONE 
    }

    override fun getItemCount() = days.size
}
