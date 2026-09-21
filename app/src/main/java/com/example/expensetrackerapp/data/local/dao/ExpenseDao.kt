package com.example.expensetrackerapp.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.expensetrackerapp.data.local.entity.ExpenseEntity

@Dao    // meaning of these means this interface has database operation. without @Dao room ignore this class
interface ExpenseDao {  // why we choose interface because room generates it implementation for us. Hence no class.

    @Insert
    suspend fun insertExpense(expense: ExpenseEntity) : Long

    @Update     // ROOM automatically update the record that has same primary key
    suspend fun updateExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id= :expenseId")     // ROOM delete the record for that matching primary key
    suspend fun deleteExpense(expenseId: Long)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpense(): Flow<List<ExpenseEntity>>

    @Query("SELECT *FROM expenses WHERE id= :expenseId")
    suspend fun getExpenseById(expenseId: Long): ExpenseEntity?
}