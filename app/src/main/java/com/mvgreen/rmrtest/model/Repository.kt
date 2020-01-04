package com.mvgreen.rmrtest.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mvgreen.rmrtest.UnsplashApplication
import com.mvgreen.rmrtest.model.network.UnsplashApi
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import java.io.IOException
import kotlin.concurrent.thread

object Repository {

    private data class LiveList<T : ResultListItem>(
        val liveDataList: MutableLiveData<List<T>?> = MutableLiveData(),
        var listPage: Int = 1,
        val updateFun: (page: Int) -> List<T>?
    )

    private val unsplashApi: UnsplashApi by lazy { UnsplashApplication.instance.unsplashApi }

    private val photoList = LiveList { page ->
        try {
            with(unsplashApi.searchPhotos(currentQuery, page).execute()) {
                return@LiveList if (isSuccessful && body() != null) body()!!.results
                else null
            }
        } catch (e: IOException) {
            return@LiveList null
        }
    }
    private val collectionList = LiveList { page ->
        try {
            with(unsplashApi.searchCollections(currentQuery, page).execute()) {
                return@LiveList if (isSuccessful && body() != null) body()!!.results
                else null
            }
        } catch (e: IOException) {
            return@LiveList null
        }
    }
    private val contentList = LiveList { page ->
        try {
            with(unsplashApi.showCollection(currentCollectionId, page).execute()) {
                return@LiveList if (isSuccessful && body() != null) body()
                else null
            }
        } catch (e: IOException) {
            return@LiveList null
        }
    }

    val searchPhotoResult: LiveData<List<UnsplashPhoto>?>
        get() = photoList.liveDataList
    val searchCollectionResult: LiveData<List<UnsplashCollection>?>
        get() = collectionList.liveDataList
    val collectionContent: LiveData<List<UnsplashPhoto>?>
        get() = contentList.liveDataList

    private var currentQuery: String = ""
    private var currentCollectionId: Int = -1

    fun performSearch(query: String) {
        currentQuery = query
        photoList.listPage = 1
        collectionList.listPage = 1
        thread {
            photoList.apply { liveDataList.postValue(updateFun(1)) }
        }
        thread {
            collectionList.apply { liveDataList.postValue(updateFun(1)) }
        }
    }

    fun openCollection(id: Int) {
        currentCollectionId = id
        contentList.listPage = 1
        thread {
            contentList.apply { liveDataList.postValue(updateFun(1)) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ResultListItem> loadNext(liveData: LiveData<List<T>?>) {
        if (currentQuery.isEmpty())
            throw IllegalStateException("Query is empty!")
        val liveList = when {
            liveData === photoList.liveDataList -> photoList
            liveData === collectionList.liveDataList -> collectionList
            liveData === contentList.liveDataList -> contentList
            else -> throw IllegalArgumentException("Parameter 'liveData' must be one of Repository's properties")
        }
        thread {
            liveList.listPage++
            val response = liveList.updateFun(liveList.listPage)
            @Suppress("UNCHECKED_CAST")
            val oldData = liveList.liveDataList.value ?: listOf()
            val newData = oldData + (response ?: listOf())
            (liveList.liveDataList as MutableLiveData<List<ResultListItem>>).postValue(newData)
        }
    }
}