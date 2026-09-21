package com.example.expensetrackerapp.ui.expense

import com.example.expensetrackerapp.data.local.entity.ExpenseEntity
import com.example.expensetrackerapp.ui.expense.presentation.model.ExpenseUiModel

data class ExpenseUiState(
    val editingExpenseId: Long? = null,
    val amount: String = "",
    val selectedCategory: String = "",
    val selectedPaymentMethod: String = "",
    val note: String = "",
    val date: Long = System.currentTimeMillis(),
    val expenses: List<ExpenseUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)