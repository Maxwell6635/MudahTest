package com.example.mudahtest.util

import com.example.mudahtest.util.DateFormatterUtils.DATEFORMAT.SERVER_TIME_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormatterUtils {

    object DATEFORMAT {
        const val SERVER_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val HH_MM = "hh:mm a"
    }

    fun getFormattedCurrentDay(): String? {
        val dateFormat = SimpleDateFormat(SERVER_TIME_FORMAT, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("gmt")
        return dateFormat.format(Date())
    }

    fun convertDateToSpecificFormat(
        date: String?,
        currentDateFormat: String,
        newDateFormat: String
    ): String? {
        try {
            if (date.isNullOrEmpty()) return ""
            val dateFormat = SimpleDateFormat(currentDateFormat, Locale.getDefault())
            val convertedDateFormat = SimpleDateFormat(newDateFormat, Locale.getDefault())
            val currentDate = dateFormat.parse(date)
            if (currentDate != null) {
                return convertedDateFormat.format(currentDate)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return date
        }
        return date
    }
}