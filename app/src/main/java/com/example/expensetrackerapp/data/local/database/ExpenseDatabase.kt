package com.example.expensetrackerapp.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expensetrackerapp.data.local.dao.ExpenseDao
import com.example.expensetrackerapp.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false
)

//@Database : tells this class represents the application database
abstract class ExpenseDatabase : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao  // this is bridge between database and dao
}