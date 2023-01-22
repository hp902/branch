package com.example.branch.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    companion object {

        fun convertDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm aaa", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()
            return outputFormat.format(date)
        }

        fun convertDateStringToRelativeTime(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)
            val currentCalendar = Calendar.getInstance()
            val inputCalendar = Calendar.getInstance()
            inputCalendar.time = date
            inputCalendar.timeZone = TimeZone.getDefault()
            val timeDiff = currentCalendar.timeInMillis - inputCalendar.timeInMillis
            val seconds = timeDiff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30
            val years = days / 365

            return when {
                seconds < 60 -> "$seconds sec ago"
                minutes < 60 -> "$minutes min ago"
                hours < 24 -> "$hours hrs ago"
                days < 30 -> "$days days ago"
                months < 12 -> "$months months ago"
                else -> "$years years ago"
            }
        }
    }
}