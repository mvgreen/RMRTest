package com.mvgreen.rmrtest.viewmodel

import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

/**
 * ViewModel of CollectionContentActivity.
 */
class CollectionContentViewModel : UnsplashViewModel() {

    // ID of the collection
    private var id = -1
    /** Title of the collection */
    var collectionTitle: String = "..."
    /** LiveList that is displayed in the RecyclerView of the activity. */
    val collectionContent: LiveList<UnsplashPhoto> = LiveList()

    /**
     * Load collection by ID.
     * @param id collection ID.
     */
    fun openCollection(id: Int) {
        this.id = id
        Repository.loadCollection(id, 1, collectionContent)
    }

    /**
     * @see [UnsplashViewModel.loadNextPageOf]
     */
    override fun <T : ResultListItem> loadNextPageOf(source: LiveList<T>) {
        if (collectionContent != source)
            throw IllegalArgumentException("Unknown LiveData object")
        Repository.loadCollection(id, collectionContent.listPage + 1, collectionContent)
    }

}