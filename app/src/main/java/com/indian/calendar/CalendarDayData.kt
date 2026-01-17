package com.indian.calendar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class CalendarDayData(
    @SerializedName("ENGLISH") val Date: String = "",
    @SerializedName("ગુજરાતી (Gujarati)") val Gujarati: String = "",
    @SerializedName("हिन्दी (Hindi)") val Hindi: String = "",
    @SerializedName("ઇસ્લામિક (Islamic - العربية)") val Islamic: String = "",
    @SerializedName("తెలుగు/ಕನ್ನಡ (Telugu/Kannada)") val TeluguKannada: String = "",
    @SerializedName("தமிழ் (Tamil)") val Tamil: String = "",
    @SerializedName("മലയാളം (Malayalam)") val Malayalam: String = "",
    @SerializedName("ਪੰਜਾਬੀ (Punjabi)") val Punjabi: String = "",
    @SerializedName("ଓଡ଼િଆ (Odia)") val Odia: String = "",
    @SerializedName("বাংলা (Bengali)") val Bengali: String = "",
    @SerializedName("नेपाली (Nepali)") val Nepali: String = "",
    @SerializedName("中文 (Chinese)") val Chinese: String = "",
    @SerializedName("עברית (Hebrew)") val Hebrew: String = "",
    @SerializedName("فارસી (Persian)") val Persian: String = "",
    @SerializedName("ኢትዮጵያ (Ethiopian)") val Ethiopian: String = "",
    @SerializedName("Basa Bali (Balinese)") val Balinese: String = "",
    @SerializedName("한국어 (Korean)") val Korean: String = "",
    @SerializedName("Tiếng Việt (Vietnamese)") val Vietnamese: String = "",
    @SerializedName("ไทย (Thai)") val Thai: String = "",
    @SerializedName("Français (French)") val French: String = "",
    @SerializedName("မြန်မာဘာသာ (Burmese)") val Burmese: String = "",
    @SerializedName("کاشمیری (Kashmiri)") val Kashmiri: String = "",
    @SerializedName("મારવાડી (Marwari)") val Marwari: String = "",
    @SerializedName("日本語 (Japanese)") val Japanese: String = "",
    @SerializedName("অসমীয়া (Assamese)") val Assamese: String = "",
    @SerializedName("سنڌી (Sindhi)") val Sindhi: String = "",
    @SerializedName("བོད་སྐད (Tibetan)") val Tibetan: String = ""
) : Parcelable
