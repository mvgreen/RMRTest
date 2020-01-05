package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class CollectionContentViewModel : ViewModel() {

    val collectionContent: LiveData<List<UnsplashPhoto>?> = Repository.collectionContent

    fun openCollection(id: Int) {
        Repository.openCollection(id)
    }

}