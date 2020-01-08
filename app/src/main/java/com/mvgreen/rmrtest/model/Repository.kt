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

/**
 * General representation of application's Model. It is used to perform API calls.
 */
object Repository {

    private val unsplashApi: UnsplashApi by lazy { UnsplashApplication.instance.unsplashApi }

    /**
     * Perform search and load results into [photoResult].
     * Result is loaded in parts, parameter [page] is used to specify the desired part.
     * This call is performed in another thread. To receive the result, subscribe to [photoResult]'s updates.
     * @param query the search query.
     * @param page result's page, every page contains 10 [UnsplashPhoto]s.
     * @param photoResult container for result.
     */
    fun searchPhotos(
        query: String,
        page: Int,
        photoResult: LiveList<UnsplashPhoto>
    ) {
        thread {
            // Try to load data
            val result: List<UnsplashPhoto>? = try {
                unsplashApi.findPhotos(query, page).execute().body()?.results
            } catch (e: IOException) {
                null
            }
            // Store it into photoResult
            photoResult.updateList(result, page)
        }
    }

    /**
     * Perform search and load results into [collectionResult].
     * Result is loaded in parts, parameter [page] is used to specify the desired part.
     * This call is performed in another thread. To receive the result, subscribe to [collectionResult]'s updates.
     * @param query the search query.
     * @param page result's page, every page contains 10 [UnsplashPhoto]s.
     * @param collectionResult container for result.
     */
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

    /**
     * Load photos from selected collection.
     * Result is loaded in parts, parameter [page] is used to specify the desired part.
     * This call is performed in another thread. To receive the result, subscribe to [content]'s updates.
     * @param id collection ID.
     * @param page result's page, every page contains 10 [UnsplashPhoto]s.
     * @param content container for result.
     */
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

    /**
     * Load 'Photo of the Day'. This call is performed in another thread.
     * To receive the result, subscribe to [content]'s updates.
     * @param content container for result.
     */
    fun getPhotoOfTheDay(content: MutableLiveData<UnsplashPhoto?>) {
        thread {
            val result: UnsplashPhoto? = try {
                val photoId = getPhotoId()
                unsplashApi.loadPhoto(photoId).execute().body()
            } catch (e: IOException) {
                null
            }
            content.postValue(result)
        }
    }

    /**
     * Parse main page's html to extract the 'Photo of the Day' id.
     */
    private fun getPhotoId(): String {
        val request: Request = Request.Builder()
            .url("https://unsplash.com")
            .build()

        val response = OkHttpClient().newCall(request).execute().body()?.string() ?: return ""

        val endIndex = response.indexOf("Photo of the Day") - 2
        val startIndex = response.lastIndexOf('/', endIndex) + 1
        return response.substring(startIndex, endIndex)
    }
}