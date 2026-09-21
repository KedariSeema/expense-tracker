package com.example.expensetrackerapp.ui.expense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetrackerapp.ui.expense.presentation.model.ExpenseUiModel
import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

@Composable
fun ExpenseScreen(
    uiState: ExpenseUiState,
    onAmountChanged: (String) -> Unit,
    onNoteChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onPaymentMethodChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: (Long) -> Unit,
    onEditExpense: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
    onSearchExpense: (String) -> Unit
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            // for search
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchExpense,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                placeholder = { Text("Search Expense......") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search,
                        contentDescription = "Search Expense")
                },
                singleLine = true
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                EditTextFieldComponent(
                    uiState,
                    onAmountChanged,
                    onNoteChanged,
                    onCategoryChanged,
                    onPaymentMethodChanged,
                    onSaveClicked,
                    onDeleteClicked,
                    onEditExpense,
                    onSearchExpense
                )
            }

            Text(text = "Expenses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                    top = 24.dp,
                    bottom = 12.dp
                ))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)) {

                items(
                    items = uiState.expenses,
                    key = { expense -> expense.id }
                ) { expense ->
                    ExpenseItem(
                        onDeleteClicked = onDeleteClicked,
                        onEditExpense = onEditExpense,
                        expense = expense
                    )
                }
            }
        }

    }
}

@Composable
fun EditTextFieldComponent(
    uiState: ExpenseUiState,
    onAmountChanged: (String) -> Unit,
    onNoteChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onPaymentMethodChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: (Long) -> Unit,
    onEditExpense: (Long) -> Unit,
    onSearchExpense: (String) -> Unit
) {
    val noteFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            text = "Add Expense",
            style = MaterialTheme.typography.titleLarge)

        // For Amount
        OutlinedTextField(
            value = uiState.amount,
            onValueChange = onAmountChanged,
            label = { Text(text = "Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    noteFocusRequester.requestFocus()
                }
            )
        )

        // For Note
        OutlinedTextField(
            value = uiState.note,
            onValueChange = onNoteChanged,
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        // add save button
        Button(
            onClick = onSaveClicked,
            modifier = Modifier
                .fillMaxWidth()
        ) { Text("Save Expense") }
    }
}

@Composable
fun ExpenseItem(
    onDeleteClicked: (Long) -> Unit,
    onEditExpense: (Long) -> Unit,
    expense: ExpenseUiModel,
    modifier: Modifier = Modifier
) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Top row: Category + Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = expense.category,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = expense.amount,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Note
            Text(
                text = expense.note,
                style = MaterialTheme.typography.bodyMedium
            )

            // Bottom row: Payment + Date + Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Payment method
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = expense.paymentMethod,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Date
                Text(
                    text = expense.date,
                    style = MaterialTheme.typography.labelMedium
                )

                // Edit
                IconButton(
                    onClick = {
                        onEditExpense(expense.id)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Expense"
                    )
                }

                // Delete
                IconButton(
                    onClick = {
                        showDeleteDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Expense"
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete Expense")
            },
            text = {
                Text("Are you sure you want to delete this expense?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClicked(expense.id)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

