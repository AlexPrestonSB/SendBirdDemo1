package com.sendbirdsampleapp

import android.app.Application
import com.sendbird.android.SendBird

class BaseApp : Application() {

    val APP_ID = "B6DCC89A-9D92-4012-B354-CC41A1BAC5DF"

    override fun onCreate() {
        super.onCreate()

        SendBird.init(APP_ID, applicationContext)
    }

}