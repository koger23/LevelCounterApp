package com.kogero.levelcounter.helpers

internal object TimeConverter {
    /*
    Converting a long number to string and show in h:m:s format
     */

    fun convert(time: Long): String {
        val hours = time / 3600
        val minutes = time % 3600 / 60
        val seconds = time % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}