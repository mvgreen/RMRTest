package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem

class LiveList<T : ResultListItem>(
    private val _liveData: MutableLiveData<List<T>?> = MutableLiveData(),
    var listPage: Int = 1
) {
    val value = _liveData.value
    val liveData: LiveData<List<T>?> = _liveData
    var isBadResult: Boolean = false
        private set
        get() {
            val result = field
            field = false
            return result
        }

    @Suppress("UNCHECKED_CAST")
    fun updateList(list: List<T>?, page: Int) {
        listPage = page
        if (list == null) {
            isBadResult = true
        }
        if (page == 1) {
            _liveData.postValue(list)
        } else {
            _liveData.postValue((_liveData.value ?: listOf()) + (list ?: listOf()))
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in List<T>?>) {
        _liveData.observe(owner, observer)
    }
}
