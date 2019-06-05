package com.sendbirdsampleapp

import android.app.Activity
import android.app.Application
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.di.component.AppComponent
import com.sendbirdsampleapp.util.AppConstants
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApp : Application(), HasActivityInjector {


    @Inject
    lateinit internal var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityDispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        SendBird.init(AppConstants.APP_ID, applicationContext)

        //AppComponent. TODO 
    }


}