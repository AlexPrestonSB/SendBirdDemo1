package com.sendbirdsampleapp.data.preferences

import com.sendbird.android.ConnectionManager

interface PreferenceHelper {

    fun getUserId(): String

    fun setUserId(userId: String?)

    fun getNickname(): String

    fun setNickname(nickname: String?)

    fun getConnected(): Boolean

    fun setConnected(connected: Boolean)
}