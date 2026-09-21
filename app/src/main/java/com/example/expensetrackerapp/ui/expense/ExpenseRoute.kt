package com.example.expensetrackerapp.ui.expense

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.expensetrackerapp.ui.expense.presentation.event.ExpenseUiEvent

@Composable
fun ExpenseRoute(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

            when(event) {
                is ExpenseUiEvent.ShowError -> {snackbarHostState.showSnackbar(event.message)}
                is ExpenseUiEvent.ShowSuccess -> {snackbarHostState.showSnackbar(event.message)}
            }
        }
    }

    ExpenseScreen(
        uiState = uiState,
        onAmountChanged = viewModel::onAmountChanged,
        onNoteChanged = viewModel::onNoteChanged,
        onSaveClicked = viewModel::saveExpense,
        onCategoryChanged = viewModel::onCategoryChanged,
        onPaymentMethodChanged = viewModel::onPaymentMethodChanged,
        onDeleteClicked = viewModel::onExpenseDelete,
        onEditExpense = viewModel::onExpenseEdit,
        snackbarHostState= snackbarHostState,
        onSearchExpense = viewModel::onSearchExpense
    )
}