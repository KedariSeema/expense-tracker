package com.example.expensetrackerapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    private val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )
    fun formatDate(timeStamp: Long): String {
        return formatter.format(Date(timeStamp))
    }
}