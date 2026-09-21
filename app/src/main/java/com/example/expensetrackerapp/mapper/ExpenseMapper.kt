package com.example.expensetrackerapp.mapper

import com.example.expensetrackerapp.data.local.entity.ExpenseEntity
import com.example.expensetrackerapp.ui.expense.presentation.model.ExpenseUiModel
import com.example.expensetrackerapp.utils.CurrencyFormatter
import com.example.expensetrackerapp.utils.DateFormatter

fun ExpenseEntity.toUiModel(): ExpenseUiModel{

    return ExpenseUiModel(
        id = id,
        amount = CurrencyFormatter.formatCurrency(amount),
        category = category,
        paymentMethod = paymentMethod,
        note = note,
        date = DateFormatter.formatDate(date),
    )
}
