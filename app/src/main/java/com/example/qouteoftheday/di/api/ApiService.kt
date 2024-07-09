package com.example.qouteoftheday.di.api

import com.example.qouteoftheday.models.Quote
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("random")
    suspend fun getRandomQuote(): Response<List<Quote>>

    @GET("today")
    suspend fun getQuoteOfTheDay(): Response<List<Quote>>
}
