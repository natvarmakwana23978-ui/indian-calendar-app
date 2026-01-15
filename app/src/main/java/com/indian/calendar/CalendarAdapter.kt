class CalendarAdapter(private val data: List<CalendarData>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTithi: TextView = view.findViewById(R.id.tvTithi)
        val tvFestival: TextView = view.findViewById(R.id.tvFestival)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.tvDate.text = item.englishDate
        holder.tvTithi.text = item.tithi ?: ""
        holder.tvFestival.text = item.festival ?: ""
    }

    override fun getItemCount(): Int = data.size
}
