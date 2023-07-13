package io.moonlighting.redditclientv2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class RedditClientApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

