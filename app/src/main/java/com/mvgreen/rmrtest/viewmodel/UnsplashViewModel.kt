package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.SearchCollectionResult
import com.mvgreen.rmrtest.model.network.json_objects.SearchPhotoResult
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class UnsplashViewModel : ViewModel() {

    /**
     * LiveData objects for possible queries to model
     */
    val searchPhotoResult: LiveData<SearchPhotoResult?> = Repository.searchPhotoResult
    val searchCollectionResult: LiveData<SearchCollectionResult?> = Repository.searchCollectionResult
    val collectionContent: LiveData<List<UnsplashPhoto>?> = Repository.collectionContent

    /**
     * Performs a new query to the model.
     */
    fun performSearch(query: String) {
        Repository.searchPhotos(query)
    }

    /**
     * Asks the model to load the next page of current search query (photos tab).
     */
    fun loadNextPhotos() {
        Repository.loadNextPhotos()
    }

    /**
     * Asks the model to load the next page of current search query (collections tab).
     */
    fun loadNextCollections() {
        Repository.loadNextCollections()
    }

    /**
     * Asks the model to load photos from selected collection
     */
    fun loadCollectionContent(id: Int) {
        Repository.loadPhotosFromCollection(id)
    }
}