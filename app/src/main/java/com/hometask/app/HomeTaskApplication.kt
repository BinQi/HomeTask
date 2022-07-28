package com.hometask.app

import android.content.Context
import androidx.multidex.MultiDexApplication

/**
 * @description Application
 * 
 * @date 2022/7/27 11:47 上午
 */
class HomeTaskApplication : MultiDexApplication() {

    companion object {
        private lateinit var sContext: Context

        val globalContext: Context
            get() {
                return sContext
            }
    }

    override fun onCreate() {
        super.onCreate()
        sContext = applicationContext
    }
}