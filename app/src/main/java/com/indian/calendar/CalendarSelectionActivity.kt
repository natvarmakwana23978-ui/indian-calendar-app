private fun fetchCalendars() {
    progressBar.visibility = View.VISIBLE
    
    val request = JsonArrayRequest(Request.Method.GET, webAppUrl, null,
        { response ->
            progressBar.visibility = View.GONE
            calendarList.clear()
            if (response.length() == 0) {
                Toast.makeText(this@CalendarSelectionActivity, "કોઈ ડેટા મળ્યો નથી", Toast.LENGTH_SHORT).show()
                return@JsonArrayRequest
            }
            for (i in 0 until response.length()) {
                val item = response.getJSONObject(i)
                // અહીં ખાતરી કરો કે સ્પેલિંગ "calendarName" જ છે
                calendarList.add(CalendarModel(item.getString("calendarName"), "Official Community"))
            }
            recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                val intent = Intent(this@CalendarSelectionActivity, LanguageSelectionActivity::class.java)
                intent.putExtra("calendar_name", selected.name)
                startActivity(intent)
            }
        },
        { error ->
            progressBar.visibility = View.GONE
            // સ્લો નેટવર્ક માટે ખાસ મેસેજ
            Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક સ્લો છે, ફરી લોડ થઈ રહ્યું છે...", Toast.LENGTH_LONG).show()
        }
    )

    // સ્લો નેટવર્ક (4G/Slow Net) માટે આ સેટિંગ્સ બહુ જરૂરી છે
    request.retryPolicy = com.android.volley.DefaultRetryPolicy(
        20000, // ૨૦ સેકન્ડ સુધી રાહ જોશે
        2,     // જો ફેલ થાય તો ૨ વાર જાતે જ પ્રયત્ન કરશે
        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )

    Volley.newRequestQueue(this).add(request)
}
