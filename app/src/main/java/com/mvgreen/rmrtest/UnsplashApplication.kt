package com.mvgreen.rmrtest

import android.app.Application
import com.mvgreen.rmrtest.network.UnsplashApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UnsplashApplication : Application() {
    private lateinit var retrofit: Retrofit
    private lateinit var _unsplashApi: UnsplashApi
    val unsplashApi: UnsplashApi
        get() = _unsplashApi

    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        _unsplashApi = retrofit.create(UnsplashApi::class.java)
    }
}