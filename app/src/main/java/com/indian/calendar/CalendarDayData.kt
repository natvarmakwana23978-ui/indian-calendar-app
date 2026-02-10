package com.smart.reminder
import com.google.gson.annotations.SerializedName

data class CalendarDayData(
    @SerializedName("date") val date: String,
    @SerializedName("data_list") val dataList: List<String>,
    @SerializedName("festival") val festival: String,
    @SerializedName("emoji") val emoji: String,
    @SerializedName("religious") val religious: String
)
