package com.mvgreen.rmrtest.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem

/** ViewModel of ListFragment. */
@Suppress("UNCHECKED_CAST")
class ListFragmentViewModel : ViewModel() {

    /** ID of this ViewModel's fragment. */
    var fragmentId = -1

    /** 'Parent' ViewModel. */
    lateinit var activityViewModel: SearchViewModel

    /** Getter for itemSource. */
    fun <T : ResultListItem> getItemSource(): LiveList<T> =
        activityViewModel.fragmentData[fragmentId].itemSource as LiveList<T>

    /** Getter for itemType. */
    fun <T : ResultListItem> getItemType(): Class<T> =
        activityViewModel.fragmentData[fragmentId].itemType as Class<T>

    /** Getter for onItemClick. */
    fun <T : ResultListItem> getOnItemClick(): (context: Context, item: T) -> Unit =
        activityViewModel.fragmentData[fragmentId].onItemClick as (Context, T) -> Unit
}