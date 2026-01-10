// ફાઈલમાં webAppUrl આ જ હોવી જોઈએ
private val webAppUrl = "https://script.google.com/macros/s/AKfycbw4BxpTd8aZEMmqVkgtVXdpco8mxBu1E9ikjKkdLdRHjBpn4QPRhMM-HCg0WsVPdGqimA/exec"

// fetchCalendars ફંક્શનમાં આ મુજબ સુધારો કરો
private fun fetchCalendars() {
    progressBar.visibility = View.VISIBLE
    val request = JsonArrayRequest(Request.Method.GET, webAppUrl, null,
        { response ->
            progressBar.visibility = View.GONE
            calendarList.clear()
            try {
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    // ખાતરી કરો કે 'calendarName' સ્પેલિંગ સાચો છે
                    calendarList.add(CalendarModel(item.getString("calendarName"), "Official"))
                }
                recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                    startActivity(Intent(this, LanguageSelectionActivity::class.java))
                }
            } catch (e: Exception) {
                // જો અહીં Error આવે તો સમજવું કે JSON માં પ્રોબ્લેમ છે
                Toast.makeText(this, "Data Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        },
        { error ->
            progressBar.visibility = View.GONE
            // જો અહીં Error આવે તો સમજવું કે નેટવર્ક કે પરમિશનનો પ્રોબ્લેમ છે
            Toast.makeText(this, "Network Error: ${error.message}", Toast.LENGTH_LONG).show()
        }
    )
    Volley.newRequestQueue(this).add(request)
}
