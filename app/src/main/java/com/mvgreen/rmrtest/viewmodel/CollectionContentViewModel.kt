package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LiveData
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class CollectionContentViewModel : UnsplashViewModel() {

    val collectionContent: LiveData<List<UnsplashPhoto>?> = Repository.collectionContent

    fun openCollection(id: Int) {
        Repository.openCollection(id)
    }

    override fun <T : ResultListItem> loadNextPageOf(source: LiveData<List<T>?>) {
        Repository.loadNext(collectionContent)
    }

}