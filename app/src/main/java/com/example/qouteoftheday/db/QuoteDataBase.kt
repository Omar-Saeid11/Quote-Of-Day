package com.example.qouteoftheday.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.qouteoftheday.models.Quote

@Database(
    entities = [Quote::class],
    version = 1
)
abstract class QuoteDataBase : RoomDatabase() {
    abstract fun getQuoteDao(): QuoteDao
}
