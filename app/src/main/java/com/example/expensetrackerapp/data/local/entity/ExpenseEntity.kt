package com.example.expensetrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")  // create table in sqlite
data class ExpenseEntity(       // we use data class as it has default method implementation toString, hashcode, equal, copy
                                // most of the database class are model hence data class
    @PrimaryKey(autoGenerate = true)  //  PrimaryKey this defines the primarykey to identify the each record uniquely,
    val id: Long = 0,                //  autoGenerate is true when we create Expense room automatically generate it
                                      // id = 0, bcoz we need to pass initial value to auto generate
    val amount: Double,

    val category: String,

    val note: String,

    val paymentMethod: String,

    val date: Long,     // we are storing date as Long bcoz it is easy to store, filter, sort, comparision.

    val createdAt: Long = System.currentTimeMillis(),

)