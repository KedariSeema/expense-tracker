package com.example.expensetrackerapp.ui.expense.presentation.model

data class ExpenseUiModel(
    val id: Long,
    val amount: String,
    val category: String,
    val note: String,
    val paymentMethod: String,
    val date: String,
)