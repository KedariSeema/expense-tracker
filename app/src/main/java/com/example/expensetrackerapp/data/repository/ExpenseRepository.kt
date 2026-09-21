package com.example.expensetrackerapp.data.repository

import com.example.expensetrackerapp.data.local.dao.ExpenseDao
import com.example.expensetrackerapp.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
){
    fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpense()

    suspend fun insertExpense(expense: ExpenseEntity): Long =  expenseDao.insertExpense(expense)

    suspend fun updateExpense(expense: ExpenseEntity)  = expenseDao.updateExpense(expense)

    suspend fun deleteExpense(expenseId: Long) = expenseDao.deleteExpense(expenseId)
    suspend fun getExpenseById(expenseId: Long): ExpenseEntity? = expenseDao.getExpenseById(expenseId)
}