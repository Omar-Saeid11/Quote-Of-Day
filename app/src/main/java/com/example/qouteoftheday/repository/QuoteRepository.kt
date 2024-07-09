package com.example.qouteoftheday.repository

import com.example.qouteoftheday.di.api.ApiService
import com.example.qouteoftheday.db.QuoteDao
import com.example.qouteoftheday.models.Quote
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val dao: QuoteDao,
    private val api: ApiService
) {
    suspend fun getRandomQuote() = api.getRandomQuote()

    suspend fun getQuoteOfTheDay() = api.getQuoteOfTheDay()

    suspend fun upsert(quote: Quote) = dao.upsert(quote)

    suspend fun deleteQuote(quote: Quote) =
        dao.deleteSavedQuote(quote)

    fun getSavedQuotes() = dao.getSavedQuotes()
}
