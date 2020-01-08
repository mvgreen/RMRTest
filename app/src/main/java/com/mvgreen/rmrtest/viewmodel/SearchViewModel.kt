package com.mvgreen.rmrtest.viewmodel

import android.content.Context
import android.content.Intent
import com.mvgreen.rmrtest.EXTRA_COLLECTION_ID
import com.mvgreen.rmrtest.EXTRA_COLLECTION_TITLE
import com.mvgreen.rmrtest.EXTRA_PHOTO_INFO
import com.mvgreen.rmrtest.activity.CollectionContentActivity
import com.mvgreen.rmrtest.activity.FullScreenPhotoActivity
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

/**
 * ViewModel of SearchActivity.
 */
class SearchViewModel : UnsplashViewModel() {

    /**
     * Container for fragment-specific data.
     * @property itemSource [LiveList] that is displayed in the fragment.
     * @property itemType type of items in [itemSource].
     * @property onItemClick listener for items in list.
     */
    data class FragmentData<T : ResultListItem>(
        val itemSource: LiveList<T>,
        val itemType: Class<T>,
        val onItemClick: (Context, T) -> Unit
    )

    /** Last requested search query. */
    lateinit var searchQuery: String

    // LiveLists with search results for both fragments
    private val foundPhotoList: LiveList<UnsplashPhoto> = LiveList()
    private val foundCollectionList: LiveList<UnsplashCollection> = LiveList()

    /**
     * List with [FragmentData] objects.
     * List of found photos is stored in the first container, list of found collections is stored in the second.
     */
    val fragmentData =
        listOf<FragmentData<out ResultListItem>>(
            FragmentData(foundPhotoList, UnsplashPhoto::class.java) { context, item ->
                context.startActivity(Intent(context, FullScreenPhotoActivity::class.java).apply {
                    putExtra(
                        EXTRA_PHOTO_INFO,
                        item
                    )
                })
            },
            FragmentData(foundCollectionList, UnsplashCollection::class.java) { context, item ->
                context.startActivity(Intent(context, CollectionContentActivity::class.java).apply {
                    putExtra(EXTRA_COLLECTION_ID, item.id)
                    putExtra(EXTRA_COLLECTION_TITLE, item.title)
                })
            }
        )


    /**
     * Perform a new search query.
     * @param query new query.
     */
    fun newQuery(query: String) {
        searchQuery = query
        Repository.searchPhotos(searchQuery, 1, foundPhotoList)
        Repository.searchCollections(searchQuery, 1, foundCollectionList)
    }

    /**
     * Load the next page of the given list.
     * @param T type of items in the [source].
     * @param source list to be updated.
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