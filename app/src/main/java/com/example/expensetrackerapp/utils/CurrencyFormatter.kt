package com.example.expensetrackerapp.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    fun formatCurrency(amount: Double): String{
        val formatter = NumberFormat.getCurrencyInstance(
            Locale("en", "IN")
        )

        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 2

        return formatter.format(amount)
    }
}