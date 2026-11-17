package com.mobilenext.playground

import android.app.Application
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

// Holds the app's one OkHttpClient. It is built when the process starts, the way most
// apps do it, so a tool that attaches later finds a client that already exists.
class PlaygroundApplication : Application() {
    companion object {
        lateinit var httpClient: OkHttpClient
            private set
    }

    override fun onCreate() {
        super.onCreate()
        httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}
