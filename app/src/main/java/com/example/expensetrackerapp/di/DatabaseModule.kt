package com.example.expensetrackerapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.expensetrackerapp.data.local.dao.ExpenseDao
import com.example.expensetrackerapp.data.local.database.ExpenseDatabase

@Module // this says that This file contains objects that I know how to create
@InstallIn(SingletonComponent::class) // These dependencies should live as long as the application
object DatabaseModule {

    @Provides   //Whenever someone asks for this type, call this function
    @Singleton
    // For a database, we always use the Application Context because it lives for the lifetime of the app and doesn't cause memory leaks.
    fun provideExpenseDatabase(@ApplicationContext context : Context): ExpenseDatabase {
        return Room.databaseBuilder(context, ExpenseDatabase::class.java, "expense_database").build()
    }

    @Provides   // Whenever someone asks for this type, call this function
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao = database.expenseDao()
}