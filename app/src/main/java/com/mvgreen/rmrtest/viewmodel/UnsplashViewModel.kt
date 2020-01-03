package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class UnsplashViewModel : ViewModel() {

    private lateinit var searchPhotoResult: LiveData<List<UnsplashPhoto>>

    lateinit var searchCollectionResult: LiveData<PagedList<UnsplashCollection>>

    private lateinit var collectionPhotos: LiveData<PagedList<UnsplashPhoto>>

    fun getSearchResult(query: String, page: Int) : LiveData<List<UnsplashPhoto>> {
        TODO()
    }
}