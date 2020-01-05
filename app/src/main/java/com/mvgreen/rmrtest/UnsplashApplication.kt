package com.mvgreen.rmrtest

import android.app.Application
import com.mvgreen.rmrtest.model.network.UnsplashApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val EXTRA_PHOTO_URL = "EXTRA_PHOTO_URL"
val EXTRA_COLLECTION_ID = "EXTRA_COLLECTION_ID"

class UnsplashApplication : Application() {

    companion object {
        lateinit var instance : UnsplashApplication
    }

    private lateinit var retrofit: Retrofit
    private lateinit var _unsplashApi: UnsplashApi
    val unsplashApi: UnsplashApi
        get() = _unsplashApi

    override fun onCreate() {
        super.onCreate()
        instance = this
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        _unsplashApi = retrofit.create(UnsplashApi::class.java)
    }
}