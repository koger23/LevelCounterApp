package com.kogero.levelcounter.helpers

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

internal object TimeConverter {
    /*
    Converting a long number to string and show in h:m:s format
     */

    fun convertTimeFromLong(time: Long): String {
        val hours = time / 3600
        val minutes = time % 3600 / 60
        val seconds = time % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun fromStringToDate(stringDate: String, pattern: String): Date? {
        val format = SimpleDateFormat(pattern)
        try {
            return format.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun convertDateToLong(date: Date?) : Long {
        return date!!.time
    }
}