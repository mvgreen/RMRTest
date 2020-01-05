package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem

abstract class UnsplashViewModel : ViewModel() {
    abstract fun <T : ResultListItem> loadNextPageOf(source: LiveData<List<T>?>)
}