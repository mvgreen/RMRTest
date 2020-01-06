package com.mvgreen.rmrtest.viewmodel

import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class CollectionContentViewModel : UnsplashViewModel() {

    private var id = -1
    var collectionTitle: String = "..."
    val contentList: LiveList<UnsplashPhoto> = LiveList()
    val collectionContent: LiveList<UnsplashPhoto> = contentList

    fun openCollection(id: Int) {
        this.id = id
        Repository.loadCollection(id, 1, contentList)
    }

    override fun <T : ResultListItem> loadNextPageOf(source: LiveList<T>) {
        if (contentList != source)
            throw IllegalArgumentException("Unknown LiveData object")
        Repository.loadCollection(id, contentList.listPage + 1, contentList)
    }

}