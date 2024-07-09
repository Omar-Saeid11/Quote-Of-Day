package com.example.qouteoftheday.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.qouteoftheday.models.Quote

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(quote: Quote): Long

    @Query("SELECT * FROM quotes")
    fun getSavedQuotes(): LiveData<List<Quote>>

    @Delete
    suspend fun deleteSavedQuote(quote: Quote)
}
