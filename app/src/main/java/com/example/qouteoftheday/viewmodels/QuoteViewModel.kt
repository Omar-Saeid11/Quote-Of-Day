package com.example.qouteoftheday.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qouteoftheday.models.Quote
import com.example.qouteoftheday.repository.QuoteRepository
import com.example.qouteoftheday.util.CheckInternet
import com.example.qouteoftheday.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val internet: CheckInternet,
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    val quote: MutableLiveData<Resource<Quote>> = MutableLiveData()
    val bookmarked: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        getRandomQuote()
    }

    fun getRandomQuote() = viewModelScope.launch {
        delay(400)
        bookmarked.postValue(false)
        safeQuotesCall()
    }

    private suspend fun safeQuotesCall() {
        try {
            if (internet.hasInternetConnection()) {
                quote.postValue(Resource.Loading())
                val response = quoteRepository.getRandomQuote()
                quote.postValue(handleQuoteResponse(response))
            } else {
                quote.postValue(Resource.Error("No internet connection."))
            }
        } catch (t: Throwable) {
            when (t) {
                is java.io.IOException -> quote.postValue(Resource.Error("Network Failure"))
                else -> quote.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleQuoteResponse(response: Response<List<Quote>>): Resource<Quote> {
        if (response.isSuccessful) {
            return Resource.Success(response.body()!![0])
        }
        return Resource.Error(response.message())
    }

    fun saveQuote(quote: Quote) = viewModelScope.launch {
        quoteRepository.upsert(quote)
        bookmarked.postValue(true)
    }

    fun getSavedQuotes() = quoteRepository.getSavedQuotes()

    fun deleteQuote(quote: Quote) = viewModelScope.launch {
        quoteRepository.deleteQuote(quote)
        bookmarked.postValue(false)
    }
}
