package com.mvgreen.rmrtest.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.SearchView
import com.mvgreen.rmrtest.EXTRA_COLLECTION_ID
import com.mvgreen.rmrtest.EXTRA_PHOTO_URL
import com.mvgreen.rmrtest.activity.CollectionContentActivity
import com.mvgreen.rmrtest.activity.FullscreenActivity
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class SearchViewModel : UnsplashViewModel() {

    data class FragmentData<T : ResultListItem>(
        val itemSource: LiveList<T>,
        val itemType: Class<T>,
        val onItemClick: (Context, T) -> Unit
    )

    lateinit var searchView: SearchView
    lateinit var searchQuery: String

    private val foundPhotoList: LiveList<UnsplashPhoto> = LiveList()
    private val foundCollectionList: LiveList<UnsplashCollection> = LiveList()

    val fragmentData =
        listOf<FragmentData<out ResultListItem>>(
            FragmentData(foundPhotoList, UnsplashPhoto::class.java) { context, item ->
                context.startActivity(Intent(context, FullscreenActivity::class.java).apply {
                    putExtra(
                        EXTRA_PHOTO_URL,
                        item.urls.raw
                    )
                })
            },
            FragmentData(foundCollectionList, UnsplashCollection::class.java) { context, item ->
                context.startActivity(Intent(context, CollectionContentActivity::class.java).apply {
                    putExtra(EXTRA_COLLECTION_ID, item.id)
                })
            }
        )


    /**
     * Performs a new query to the model.
     */
    fun newQuery(query: String) {
        searchQuery = query
        foundPhotoList.listPage = 1
        foundCollectionList.listPage = 1
        Repository.searchPhotos(searchQuery, 1, foundPhotoList)
        Repository.searchCollections(searchQuery, 1, foundCollectionList)
    }

    /**
     * Loads next page of the given list.
     */
    override fun <T : ResultListItem> loadNextPageOf(source: LiveList<T>) {
        when (source) {
            foundPhotoList ->
                Repository.searchPhotos(searchQuery, foundPhotoList.listPage + 1, foundPhotoList)
            foundCollectionList ->
                Repository.searchCollections(searchQuery, foundCollectionList.listPage + 1, foundCollectionList)
            else ->
                throw IllegalArgumentException("Unknown LiveData object")
        }
    }

}