package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val languages = arrayOf(
        "English", "ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "मરાઠી (Marathi)", "ਪੰજાબી (Punjabi)",
        "বাংলা (Bengali)", "తెలుగు (Telugu)", "தமிழ் (Tamil)", "ಕನ್ನಡ (Kannada)", "മലയാളം (Malayalam)",
        "ଓଡ଼િଆ (Odia)", "संस्कृतम् (Sanskrit)", "Español", "Français", "العربية", "中文 (Chinese)",
        "日本語 (Japanese)", "Deutsch", "Português", "Русский", "한국어 (Korean)", "Italiano",
        "Türkçe", "ไทย (Thai)", "עברית (Hebrew)", "فારસી (Persian)", "অসমીયા (Assamese)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        val spinnerCalendar = findViewById<Spinner>(R.id.calendarSpinner)
        val spinnerLanguage = findViewById<Spinner>(R.id.languageSpinner)
        val btnNext = findViewById<Button>(R.id.btnOpenCalendar)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        spinnerCalendar.adapter = adapter
        spinnerLanguage.adapter = adapter

        btnNext.setOnClickListener {
            val selectedCalIndex = spinnerCalendar.selectedItemPosition
            val selectedLang = spinnerLanguage.selectedItem.toString()

            btnNext.isEnabled = false
            btnNext.text = "લોડિંગ..."

            val apiService = RetrofitClient.instance.create(ApiService::class.java)
            apiService.getCalendarData("Sheet1", "readAll").enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        val intent = Intent(this@MainActivity, CalendarViewActivity::class.java)
                        intent.putExtra("DATA", response.body().toString())
                        intent.putExtra("SELECTED_LANG", selectedLang)
                        intent.putExtra("CAL_INDEX", selectedCalIndex)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "ડેટા નથી મળ્યો", Toast.LENGTH_SHORT).show()
                    }
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "એરર: ${t.message}", Toast.LENGTH_SHORT).show()
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }
            })
        }
    }
}
