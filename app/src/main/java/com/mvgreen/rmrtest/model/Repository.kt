package com.mvgreen.rmrtest.model

import androidx.lifecycle.MutableLiveData
import com.mvgreen.rmrtest.model.network.json_objects.SearchCollectionResult
import com.mvgreen.rmrtest.model.network.json_objects.SearchPhotoResult
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

object Repository {

    val searchPhotoResult: MutableLiveData<SearchPhotoResult?> = MutableLiveData()
    val searchCollectionResult: MutableLiveData<SearchCollectionResult?> = MutableLiveData()
    val collectionContent: MutableLiveData<List<UnsplashPhoto>?> = MutableLiveData()
    private lateinit var currentQuery: String

    fun searchPhotos(query: String) {
        TODO()
//        if (!this::searchResult.isInitialized)
//            searchResult = liveData {
//                val response = UnsplashApplication.instance.unsplashApi.searchPhotos(query).execute()
//                if (response.isSuccessful && response.body() != null)
//                    emit(response.body()!!)
//                else
//                    emit(SearchPhotoResult(0, 0, listOf()))
//            }
//        return searchResult
    }

    fun loadNextPhotos() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loadNextCollections() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loadPhotosFromCollection(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}