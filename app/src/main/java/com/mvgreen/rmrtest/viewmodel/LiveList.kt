package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem

/**
 * LiveData-like container for a list that processes its updates and tracks the current page.
 * @param T type of the items in the list.
 */
class LiveList<T : ResultListItem> {
    // Actual container that holds the list
    private val liveData: MutableLiveData<List<T>?> = MutableLiveData()

    /** The last page that was added to this list. */
    var listPage: Int = 1
        private set

    /** Field to access the list itself. */
    val value = liveData.value

    /**
     * Flag that indicates that the result was empty due to a request error, not because the response itself was empty.
     * Resets after read operation.
     */
    var isBadResult: Boolean = false
        private set
        get() {
            val result = field
            field = false
            return result
        }

    /**
     * Update the list with new items. If [page] is not equal to 1, the elements will be added to the list,
     * otherwise the whole list will be replaced with a new one.
     * @param newElements elements to supplement or replace the list.
     * @param page a page of the response, if 1, the list will be completely replaced with [newElements].
     */
    fun updateList(newElements: List<T>?, page: Int) {
        listPage = page
        if (newElements == null) {
            isBadResult = true
        }
        if (page == 1) {
            liveData.postValue(newElements)
        } else {
            liveData.postValue((liveData.value ?: listOf()) + (newElements ?: listOf()))
        }
    }

    /**
     * Wrapper to observe updates of [liveData].
     * @param owner a [LifecycleOwner] that observes [value] updates.
     * @param observer a callback that will be called after every update.
     */
    fun observe(owner: LifecycleOwner, observer: Observer<in List<T>?>) {
        liveData.observe(owner, observer)
    }
}
