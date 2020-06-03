package com.sendbirdsampleapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sendbird.android.SendBird
import com.sendbird.calls.DirectCall
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.handler.SendBirdCallListener
import com.sendbirdsampleapp.di.component.AppComponent
import com.sendbirdsampleapp.di.component.DaggerAppComponent
import com.sendbirdsampleapp.ui.calls.VideoCallActivity
import com.sendbirdsampleapp.ui.calls.VoiceCallActivity
import com.sendbirdsampleapp.util.AppConstants
import java.util.*

class BaseApp : Application() {

    private var isSyncManagerSetup = false
    private val CALLEE_ID = "CALLEE_ID"
    private val CALL_TYPE = "IS_VIDEO"
    private val INCOMMING = "INCOMMING"
    private val CALL_ID = "CALL_ID"


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
//                        if (CallActivity.isRunning) {
//                            call.end()
//                            return
//                        }

                        if (call.isVideoCall) {
                            val intent = Intent(applicationContext, VideoCallActivity::class.java)
                            intent.putExtra(CALLEE_ID, call.callee.userId)
                            intent.putExtra(CALL_TYPE, true)
                            intent.putExtra(CALL_ID, call.callId)
                            intent.putExtra(INCOMMING, true)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

                            startActivity(intent)
                        } else {
                            val intent = Intent(applicationContext, VoiceCallActivity::class.java)
                            intent.putExtra(CALLEE_ID, call.callee.userId)
                            intent.putExtra(CALL_ID, call.callId)
                            intent.putExtra(CALL_TYPE, false)
                            intent.putExtra(INCOMMING, true)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

                            startActivity(intent)
                        }
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