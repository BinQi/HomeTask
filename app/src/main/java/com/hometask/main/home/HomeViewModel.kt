package com.hometask.main.home

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @description
 */
class HomeViewModel : ViewModel() {
    private val _homePageRefreshLiveData by lazy { MutableLiveData<Long>() }
    val homePageRefreshLiveData: LiveData<Long>
        get() {
            return _homePageRefreshLiveData
        }

    fun refreshHomePage() {
        _homePageRefreshLiveData.postValue(SystemClock.elapsedRealtime())
    }
}