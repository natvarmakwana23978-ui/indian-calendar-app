package your.package.name // અહીં તમારું પેકેજ નેમ લખવું

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerLang = findViewById<Spinner>(R.id.spinnerLanguage)
        val spinnerCal = findViewById<Spinner>(R.id.spinnerCalendar)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // ભાષા લિસ્ટ
        val languages = arrayOf("English", "ગુજરાતી", "Hindi")
        spinnerLang.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        // ૨૨ કેલેન્ડર કી (JSON મુજબ)
        val calendars = arrayOf("islamic", "hebrew", "persian", "indian_civil", "buddhist", "jain", "mayan", "nepal_samvat")
        spinnerCal.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)

        btnSave.setOnClickListener {
            val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            prefs.edit().putString("selected_calendar", spinnerCal.selectedItem.toString()).apply()
            prefs.edit().putString("app_lang", spinnerLang.selectedItem.toString()).apply()
            Toast.makeText(this, "સેટિંગ્સ સેવ થયા!", Toast.LENGTH_SHORT).show()
        }
    }
}

