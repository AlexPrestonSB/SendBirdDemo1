package com.sendbirdsampleapp.data.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdException
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.util.PushUtil


class FirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (SendBirdCall.handleFirebaseMessageData(remoteMessage.data)) {
            Log.d(
                "TAG",
                "[SendBirdCall Message] onMessageReceived() => " + remoteMessage.data.toString()
            )
        } else {
            Log.d(
                "TAG",
                "onMessageReceived() => From: " + remoteMessage.from
            )
            if (remoteMessage.data.size > 0) {
                Log.d(
                    "TAG",
                    "onMessageReceived() => Data: " + remoteMessage.data.toString()
                )
            }
            if (remoteMessage.notification != null) {
                Log.d(
                    "TAG",
                    "onMessageReceived() => Notification Body: " + remoteMessage.notification!!.body
                )
            }
        }
    }

    override fun onNewToken(token: String) {

        if (SendBirdCall.getCurrentUser() != null) {
            PushUtil.registerCallPushToken(applicationContext, token, object : PushUtil.PushTokenHandler{
                override fun onResult(e: SendBirdException?) {
                    if(e != null) {
                        Log.e("TAG", "Failed to register token")
                    }
                }
            })
        } else {

            AppPreferenceHelper(this).setCallToken(token)
        }

    }



}