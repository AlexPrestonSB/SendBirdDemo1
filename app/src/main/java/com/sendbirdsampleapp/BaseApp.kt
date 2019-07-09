package com.sendbirdsampleapp

import android.app.Application
import android.content.Context
import com.sendbird.android.SendBird
import com.sendbird.syncmanager.SendBirdSyncManager
import com.sendbirdsampleapp.di.component.AppComponent
import com.sendbirdsampleapp.di.component.DaggerAppComponent
import com.sendbirdsampleapp.util.AppConstants

class BaseApp : Application() {


    override fun onCreate() {
        super.onCreate()

        SendBird.init(AppConstants.APP_ID, applicationContext)
        SendBirdSyncManager.setLoggerLevel(98765)

        injector = DaggerAppComponent.builder()
            .application(this)
            .build()

    }

    lateinit var injector: AppComponent

    companion object {
        @JvmStatic
        fun app(context: Context) =  context.applicationContext as BaseApp
    }

}