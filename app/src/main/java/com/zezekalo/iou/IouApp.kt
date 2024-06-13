package com.zezekalo.iou

import android.app.Application
import com.zezekalo.iou.log.CustomDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class IouApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(CustomDebugTree())
    }

    companion object {
        internal const val GLOBAL_TAG = "IouAppTag"
    }
}
