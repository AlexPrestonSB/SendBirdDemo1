package com.sendbirdsampleapp

import android.app.Application
import android.content.Context
import android.util.Log
import com.sendbird.android.SendBird
import com.sendbird.calls.DirectCall
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.handler.SendBirdCallListener
import com.sendbirdsampleapp.di.component.AppComponent
import com.sendbirdsampleapp.di.component.DaggerAppComponent
import com.sendbirdsampleapp.ui.calls.CallActivity
import com.sendbirdsampleapp.util.ActivityUtils
import com.sendbirdsampleapp.util.AppConstants
import java.util.*

class BaseApp : Application() {

    private var isSyncManagerSetup = false

    override fun onCreate() {
        super.onCreate()

        SendBird.init(AppConstants.APP_ID, applicationContext)
        initSendBirdCall()

        injector = DaggerAppComponent.builder()
            .application(this)
            .build()

    }

    fun initSendBirdCall() {

        if (SendBirdCall.init(applicationContext, AppConstants.APP_ID)) {
            SendBirdCall.removeAllListeners()
            SendBirdCall.addListener(
                UUID.randomUUID().toString(),
                object : SendBirdCallListener() {
                    override fun onRinging(call: DirectCall) {
                        Log.d(
                            "TAG",
                            "onRinging() => callId: " + call.callId
                        )
                        if (CallActivity.isRunning) {
                            call.end()
                            return
                        }
                        ActivityUtils.startCallActivityAsCallee(context, call)
                    }
                })
        }
    }

    lateinit var injector: AppComponent

    companion object {
        @JvmStatic
        fun app(context: Context) =  context.applicationContext as BaseApp
    }

}