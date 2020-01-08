package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.*
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem

/**
 * Abstract superclass for ViewModels with [LiveList]s.
 */
abstract class UnsplashViewModel : ViewModel() {

    /**
     * Request the next page of the given list.
     * @param T type of elements in [source].
     * @param source LiveList to be updated.
     */
    abstract fun <T : ResultListItem> loadNextPageOf(source: LiveList<T>)
}