package com.mvgreen.rmrtest.model

import androidx.lifecycle.MutableLiveData
import com.mvgreen.rmrtest.UnsplashApplication
import com.mvgreen.rmrtest.model.network.UnsplashApi
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.viewmodel.LiveList
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.concurrent.thread


object Repository {

    private val unsplashApi: UnsplashApi by lazy { UnsplashApplication.instance.unsplashApi }

    @Suppress("UNCHECKED_CAST")
    fun searchPhotos(
        query: String,
        page: Int,
        photoResult: LiveList<UnsplashPhoto>
    ) {
        thread {
            val result: List<UnsplashPhoto>? = try {
                unsplashApi.findPhotos(query, page).execute().body()?.results
            } catch (e: IOException) {
                null
            }
            photoResult.updateList(result, page)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun searchCollections(
        query: String,
        page: Int,
        collectionResult: LiveList<UnsplashCollection>
    ) {
        thread {
            val result: List<UnsplashCollection>? = try {
                unsplashApi.findCollections(query, page).execute().body()?.results
            } catch (e: IOException) {
                null
            }
            collectionResult.updateList(result, page)
        }
    }

    fun loadCollection(id: Int, page: Int, content: LiveList<UnsplashPhoto>) {
        thread {
            val result: List<UnsplashPhoto>? = try {
                unsplashApi.showCollection(id, page).execute().body()
            } catch (e: IOException) {
                null
            }
            content.updateList(result, page)
        }
    }

    fun getPhotoOfTheDay(content: MutableLiveData<UnsplashPhoto?>) {
        thread {
            val result: UnsplashPhoto? = try {
                val photoId = getRealPhotoOfTheDayId()
                unsplashApi.loadPhotoOfTheDay(photoId).execute().body()
            } catch (e: IOException) {
                null
            }
            content.postValue(result)
        }
    }

    private fun getRealPhotoOfTheDayId(): String {
        val request: Request = Request.Builder()
            .url("https://unsplash.com")
            .build()

        val response = OkHttpClient().newCall(request).execute().body()?.string() ?: return ""

        val endIndex = response.indexOf("Photo of the Day") - 2
        val startIndex = response.lastIndexOf('/', endIndex) + 1
        return response.substring(startIndex, endIndex)
    }
}