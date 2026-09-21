package com.example.expensetrackerapp.ui.expense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.data.local.entity.ExpenseEntity
import com.example.expensetrackerapp.data.repository.ExpenseRepository
import com.example.expensetrackerapp.mapper.toUiModel
import com.example.expensetrackerapp.ui.expense.presentation.event.ExpenseUiEvent
import com.example.expensetrackerapp.ui.expense.presentation.model.ExpenseUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())  // only viewmodel can modify these
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()   // only to observer by ui

    private val _events = MutableSharedFlow<ExpenseUiEvent>()
    val events = _events.asSharedFlow()

    private var currentExpense: ExpenseEntity? = null
    private  var expenseList: List<ExpenseEntity> = emptyList<ExpenseEntity>()
    init{
        observeExpenses()
    }

    private fun observeExpenses(){
        viewModelScope.launch {
            expenseRepository.getAllExpenses().collect { expenses ->
                expenseList = expenses

                val expenseUIModel = expenses.map { expense ->
                    expense.toUiModel()
                }

                _uiState.update {
                    it.copy(expenses = expenseUIModel)
                }
            }
        }
    }

    fun onAmountChanged(userEnteredAmount: String) {
        _uiState.update {
            it.copy(amount = userEnteredAmount)
        }
    }

    fun onNoteChanged(userEnteredNoted: String) {
        _uiState.update {
            it.copy(
                note = userEnteredNoted
            )
        }
    }

    fun onPaymentMethodChanged(paymentMethod: String) {
        _uiState.update {
            it.copy(
                selectedPaymentMethod = paymentMethod
            )
        }
    }

    fun onDateChanged(newDate: Long){
        _uiState.update {
            it.copy(
                date = newDate
            )
        }
    }

    fun onCategoryChanged(category: String){
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    fun saveExpense(){
        viewModelScope.launch {

        val currentState = _uiState.value
        val amount = currentState.amount.toDoubleOrNull()
        if(null == amount || amount <= 0.0){
                _events.emit(
                    ExpenseUiEvent.ShowError("Please Enter valid amount")
                )
            return@launch
        }
        try{
            if(null == currentState.editingExpenseId){
                val expense = createExpenseEntity(currentState, amount)
                expenseRepository.insertExpense(expense)
            } else {
                val updatedExpense = currentExpense?.copy(
                    amount= amount,
                    paymentMethod = currentState.selectedPaymentMethod,
                    note = currentState.note,
                    category = currentState.selectedCategory,
                    date = currentState.date
                    )
                if(null != updatedExpense){
                    expenseRepository.updateExpense(updatedExpense)
                }
            }
            clearExpenseForm()
            _events.emit(
                ExpenseUiEvent.ShowSuccess("Expense Saved Successfully")
                    )
        }catch(e: Exception){
            Log.e("ExpenseApp", "ERROR: Due to ${e.message}")
            _events.emit(
                ExpenseUiEvent.ShowError("Unable to save expense. Please try again.")
                )
        }
        }
    }

    private fun clearExpenseForm() {
        _uiState.value = ExpenseUiState()
        currentExpense = null
    }

    private fun createExpenseEntity(expenseUiState: ExpenseUiState, amount: Double): ExpenseEntity =  ExpenseEntity(
                                                                                                amount = amount,
                                                                                                category = expenseUiState.selectedCategory,
                                                                                                note = expenseUiState.note,
                                                                                                paymentMethod = expenseUiState.selectedPaymentMethod,
                                                                                                date = expenseUiState.date )

    fun onExpenseDelete(expenseId: Long){
       viewModelScope.launch {
           expenseRepository.deleteExpense(expenseId)
       }
    }

    fun onExpenseEdit(expenseId: Long){
        loadExpense(expenseId)
    }

    private fun loadExpense(expenseId: Long) {
        viewModelScope.launch {
            val expense = expenseRepository.getExpenseById(expenseId)
            currentExpense = expense

            if(expense != null){
                _uiState.update {
                    it.copy(
                        editingExpenseId = expense.id,
                        amount = expense.amount.toString(),
                        selectedCategory = expense.category,
                        selectedPaymentMethod = expense.paymentMethod,
                        note = expense.note,
                        date = expense.date,
                    )
                }
            } else{
                _uiState.update {
                    it.copy(
                      //  errorMessage = "Expense not found."
                    )
                }
            }
        }
    }

    fun onSearchExpense(searchKey: String) {

        _uiState.update {
            it.copy(searchQuery = searchKey)
        }

        val filteredExpenses = filterExpenses(searchKey)

        val sortedExpenseUIModel = filteredExpenses.map { sortedEntity ->
            sortedEntity.toUiModel()
        }

        _uiState.update {
            it.copy(expenses = sortedExpenseUIModel)
        }
    }

    private fun filterExpenses(searchKey: String) : List<ExpenseEntity>{
        return expenseList.filter {

            it.note.contains(searchKey, ignoreCase = true) ||
            it.amount.toString().contains(searchKey, ignoreCase = true) ||
            it.paymentMethod.contains(searchKey, ignoreCase = true) ||
            it.category.contains(searchKey, ignoreCase = true)
        }
    }
}