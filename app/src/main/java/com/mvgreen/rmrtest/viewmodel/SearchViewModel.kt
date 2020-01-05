package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LiveData
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class SearchViewModel : UnsplashViewModel() {

    /**
     * LiveData objects for possible queries to model
     */
    val searchPhotoResult: LiveData<List<UnsplashPhoto>?> = Repository.searchPhotoResult
    val searchCollectionResult: LiveData<List<UnsplashCollection>?> = Repository.searchCollectionResult

    /**
     * Performs a new query to the model.
     */
    fun performSearch(query: String) {
        Repository.performSearch(query)
    }

    /**
     * Loads next page of the given list.
     */
    override fun <T : ResultListItem> loadNextPageOf(source: LiveData<List<T>?>) {
        Repository.loadNext(source)
    }

}