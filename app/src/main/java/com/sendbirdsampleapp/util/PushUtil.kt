package com.sendbirdsampleapp.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.sendbird.android.SendBird
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdException
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper

object PushUtil {

    fun registerPushTokenForCurrentUser(token: String, handler: SendBird.RegisterPushTokenWithStatusHandler?) {
        SendBird.registerPushTokenForCurrentUser(token, handler)
    }

    fun unregisterPushTokenForCurrentUser(token: String, handler: SendBird.UnregisterPushTokenHandler?) {
        SendBird.unregisterPushTokenForCurrentUser(token, handler)
    }

    interface GetPushTokenHandler {
        fun onResult(
            token: String?,
            e: SendBirdException?
        )
    }


    fun getPushToken(
        context: Context?,
        handler: GetPushTokenHandler?
    ) {
        Log.d("TAG", "getPushToken()")
        val savedToken: String? = context?.let { AppPreferenceHelper(it).getCallToken() }
        if (TextUtils.isEmpty(savedToken)) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task: Task<InstanceIdResult?> ->
                    if (!task.isSuccessful) {
                        Log.d(
                            "TAG",
                            "getPushToken() => getInstanceId failed",
                            task.exception
                        )
                        if (handler != null) {
                            handler.onResult(
                                null,
                                SendBirdException(if (task.exception != null) task.exception!!.message else "")
                            )
                        }
                        return@addOnCompleteListener
                    }
                    val pushToken =
                        if (task.result != null) task.result!!.token else ""
                    Log.d(
                        "TAG",
                        "getPushToken() => pushToken: $pushToken"
                    )
                    if (handler != null) {
                        handler.onResult(pushToken, null)
                    }
                }
        } else {
            Log.d(
                "TAG",
                "savedToken: $savedToken"
            )
            if (handler != null) {
                handler.onResult(savedToken, null)
            }
        }
    }


    interface PushTokenHandler {
        fun onResult(e: SendBirdException?)
    }

    fun registerCallPushToken(
        context: Context?,
        pushToken: String,
        handler: PushTokenHandler?
    ) {
        Log.d(
            "TAG",
            "registerPushToken(pushToken: $pushToken)"
        )
        SendBirdCall.registerPushToken(
            pushToken,
            false
        ) { e: SendBirdException? ->
            if (e != null) {
                Log.d(
                    "TAG",
                    "registerPushToken() => e: " + e.message
                )
                context?.let { AppPreferenceHelper(it).setCallToken(pushToken) }
                handler?.onResult(e)
                return@registerPushToken
            }
            Log.d(
                "TAG",
                "registerPushToken() => OK"
            )
            context?.let { AppPreferenceHelper(it).setCallToken(pushToken) }
            handler?.onResult(null)
        }
    }

}