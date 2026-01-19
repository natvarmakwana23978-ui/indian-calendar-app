package com.indian.calendar
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData

class CalendarDayAdapter(private val days: List<CalendarDayData>, private val colIndex: Int) : 
    RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
        val txtAlert: TextView = view.findViewById(R.id.txtAlertBanner) // XML માં ઉમેરવું પડશે
        val txtTomorrow: TextView = view.findViewById(R.id.txtTomorrowNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        holder.txtDate.text = day.Date
        holder.txtTithi.text = when(colIndex) { 1 -> day.Gujarati; 2 -> day.Hindi; else -> day.Gujarati }

        // રેડ બેનર લોજિક (ઉદાહરણ) [cite: 2026-01-17]
        holder.txtAlert.visibility = if(position == 0) View.VISIBLE else View.GONE
        
        // આવતીકાલનું રીમાઇન્ડર [cite: 2026-01-07]
        val tomorrow = days.getOrNull(position + 1)
        if (tomorrow != null && tomorrow.Gujarati.contains("બીજ")) {
            holder.txtTomorrow.visibility = View.VISIBLE
            holder.txtTomorrow.text = "આવતીકાલે ${tomorrow.Gujarati} છે, જય રામાપીર"
        } else {
            holder.txtTomorrow.visibility = View.GONE
        }
    }
    override fun getItemCount() = days.size
}
