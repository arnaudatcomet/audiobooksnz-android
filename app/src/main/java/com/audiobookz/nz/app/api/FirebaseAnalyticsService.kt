package com.audiobookz.nz.app.api

import android.app.Activity
import android.util.Log
import com.audiobookz.nz.app.util.ConversionEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class FirebaseAnalyticsService {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initAnalytic() {
        firebaseAnalytics = Firebase.analytics
    }

    fun logEvent(event: ConversionEvent, value: String) {
        firebaseAnalytics.logEvent("$event") {
            param("full_text", value)
        }
        println("## analytic $event")
    }
}