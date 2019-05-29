package com.example.sendbirdsampleapp

import android.app.Application
import com.example.sendbirdsampleapp.util.PreferenceUtils
import com.sendbird.android.SendBird

class BaseApp : Application() {

    val APP_ID = "B6DCC89A-9D92-4012-B354-CC41A1BAC5DF"
    val APP_VERSION = "0.0.05"

    override fun onCreate() {
        super.onCreate()

        SendBird.init(APP_ID, applicationContext)
    }
}