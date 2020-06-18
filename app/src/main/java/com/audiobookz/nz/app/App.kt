package com.audiobookz.nz.app

import android.app.Activity
import android.app.Application
import com.audiobookz.nz.app.di.AppInjector

import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.audioengine.mobile.AudioEngine
import io.audioengine.mobile.LogLevel
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)

        AppInjector.init(this)
        AudioEngine.init(this, "aa15eccc-d90a-488b-a19f-d4b2634c8fe0", LogLevel.VERBOSE);
    }

    override fun activityInjector() = dispatchingAndroidInjector
}