package com.indian.calendar

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var txtGregorianDate: TextView
    private lateinit var txtGregorianDay: TextView
    private lateinit var txtVikramDate: TextView
    private lateinit var txtSpecialDay: TextView
    private lateinit var txtTodayEvents: TextView
    private lateinit var txtReminders: TextView
    private lateinit var btnAddReminder: Button

    // Calendar data
    private var calendarData: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        initViews()
        
        // Load calendar data
        loadCalendarData()
        
        // Display today's information
        displayTodayInfo()
        
        // Setup button click
        setupButtonClick()
    }

    private fun initViews() {
        txtGregorianDate = findViewById(R.id.txtGregorianDate)
        txtGregorianDay = findViewById(R.id.txtGregorianDay)
        txtVikramDate = findViewById(R.id.txtVikramDate)
        txtSpecialDay = findViewById(R.id.txtSpecialDay)
        txtTodayEvents = findViewById(R.id.txtTodayEvents)
        txtReminders = findViewById(R.id.txtReminders)
        btnAddReminder = findViewById(R.id.btnAddReminder)
    }

    private fun loadCalendarData() {
        try {
            // Load JSON from assets
            val inputStream = assets.open("json/calendar_2082.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            
            val jsonString = String(buffer, Charsets.UTF_8)
            calendarData = JSONArray(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            txtTodayEvents.text = "ડેટા લોડ થતાં એરર: ${e.message}"
        }
    }

    private fun displayTodayInfo() {
        // Get current date
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val today = dateFormat.format(calendar.time)
        
        // Get day name in Gujarati
        val dayFormat = SimpleDateFormat("EEEE", Locale("gu", "IN"))
        val dayName = dayFormat.format(calendar.time)
        
        // Display Gregorian date
        val gujaratiDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("gu", "IN"))
        val gujaratiDate = gujaratiDateFormat.format(calendar.time)
        
        txtGregorianDate.text = "📅 $gujaratiDate"
        txtGregorianDay.text = "📆 $dayName"
        
        // Find today's data in JSON
        findAndDisplayTodayData(today)
        
        // Display reminders
        displayReminders()
    }

    private fun findAndDisplayTodayData(today: String) {
        calendarData?.let { data ->
            for (i in 0 until data.length()) {
                val item = data.getJSONObject(i)
                val date = item.getString("Date")
                
                if (date == today) {
                    // Display Vikram Samvat
                    val month = item.getString("Month")
                    val tithi = item.getString("Tithi")
                    txtVikramDate.text = "$month, $tithi"
                    
                    // Display special day if exists
                    val specialDay = item.getString("Special_Day")
                    if (specialDay.isNotEmpty() && specialDay != "null") {
                        txtSpecialDay.text = "✨ $specialDay"
                    } else {
                        txtSpecialDay.text = ""
                    }
                    
                    // Display today's events summary
                    val dayName = item.getString("Day")
                    txtTodayEvents.text = """
                    📌 દિવસ: $dayName
                    📌 મહિનો: $month
                    📌 તિથિ: $tithi
                    ${if (specialDay.isNotEmpty() && specialDay != "null") "📌 ખાસ: $specialDay" else ""}
                    """.trimIndent()
                    
                    return
                }
            }
            
            // If date not found
            txtVikramDate.text = "તારીખ મળી નથી"
            txtTodayEvents.text = "આજની તારીખ માટે ડેટા ઉપલબ્ધ નથી"
        }
    }

    private fun displayReminders() {
        // TODO: Load reminders from SharedPreferences or database
        val reminders = """
        • કોઈ રીમાઇન્ડર ઉમેરાયા નથી
        • "+ નવું રીમાઇન્ડર ઉમેરો" બટન પર ક્લિક કરો
        """.trimIndent()
        
        txtReminders.text = reminders
    }

    private fun setupButtonClick() {
        btnAddReminder.setOnClickListener {
            // TODO: Implement add reminder functionality
            txtReminders.text = """
            ⏳ રીમાઇન્ડર ફિચર જલ્દી આવી રહ્યું છે...
            
            આગામી અપડેટમાં:
            • જન્મદિવસ રીમાઇન્ડર
            • કાર સર્વિસ રીમાઇન્ડર
            • વીમા પ્રિમીયમ રીમાઇન્ડર
            
            વર્ઝન 1.1 માં ઉમેરાશે!
            """.trimIndent()
        }
    }
}
