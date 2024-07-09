package com.example.qouteoftheday.di

import android.content.Context
import androidx.room.Room
import com.example.qouteoftheday.di.api.ApiService
import com.example.qouteoftheday.db.QuoteDao
import com.example.qouteoftheday.db.QuoteDataBase
import com.example.qouteoftheday.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesRoomDb(@ApplicationContext context: Context): QuoteDataBase = Room.databaseBuilder(
        context.applicationContext,
        QuoteDataBase::class.java,
        "quote_db.db"
    ).build()

    @Provides
    fun providesQuoteDao(quoteDataBase: QuoteDataBase): QuoteDao = quoteDataBase.getQuoteDao()
}
