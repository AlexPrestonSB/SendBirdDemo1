package com.sendbirdsampleapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {


    fun formatDateTime(timeInMillis: Long): String {
        if (isToday(timeInMillis)) {
            return formatDateTime(timeInMillis)
        } else {
            return formatDate(timeInMillis)
        }
    }

    fun formatTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }



    fun formatDate(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }

    fun isToday(timeInMillis: Long): Boolean {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date = dateFormat.format(timeInMillis)
        return date.equals(dateFormat.format(System.currentTimeMillis()))
    }
}