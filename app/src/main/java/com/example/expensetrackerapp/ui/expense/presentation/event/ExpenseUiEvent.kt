package com.example.expensetrackerapp.ui.expense.presentation.event

sealed interface ExpenseUiEvent {

    data class ShowError(val message: String): ExpenseUiEvent
    data class ShowSuccess(val message: String): ExpenseUiEvent
}