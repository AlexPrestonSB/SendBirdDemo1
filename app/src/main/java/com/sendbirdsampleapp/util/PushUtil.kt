package com.sendbirdsampleapp.util

import com.sendbird.android.SendBird

object PushUtil {

    fun registerPushTokenForCurrentUser(token: String, handler: SendBird.RegisterPushTokenWithStatusHandler?) {
        SendBird.registerPushTokenForCurrentUser(token, handler)
    }

    fun unregisterPushTokenForCurrentUser(token: String, handler: SendBird.UnregisterPushTokenHandler?) {
        SendBird.unregisterPushTokenForCurrentUser(token, handler)
    }
}