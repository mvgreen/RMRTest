package com.mvgreen.rmrtest.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem

@Suppress("UNCHECKED_CAST")
class ListFragmentViewModel : ViewModel() {

    var fragmentId = -1
    lateinit var activityViewModel: SearchViewModel

    fun <T : ResultListItem> getItemSource(): LiveList<T> =
        activityViewModel.fragmentData[fragmentId].itemSource as LiveList<T>

    fun <T : ResultListItem> getItemType(): Class<T> =
        activityViewModel.fragmentData[fragmentId].itemType as Class<T>

    fun <T : ResultListItem> getOnItemClick(): (context: Context, item: T) -> Unit =
        activityViewModel.fragmentData[fragmentId].onItemClick as (Context, T) -> Unit
}