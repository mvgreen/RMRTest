package com.mvgreen.rmrtest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.mvgreen.rmrtest.activity.MainActivity
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto

class MainViewModel : ViewModel() {

    // 'Photo of the Day' LiveData object
    private val photoLiveData = MutableLiveData<UnsplashPhoto?>()

    /**
     * Start observing LiveData.
     * @param activity this ViewModel's activity.
     * @param observer callback object.
     */
    fun observeImage(activity: MainActivity, observer: Observer<UnsplashPhoto?>) {
        photoLiveData.observe(activity, observer)
    }

    /**
     * Request photo update.
     */
    fun getPhotoOfTheDay() {
        Repository.getPhotoOfTheDay(photoLiveData)
    }

}