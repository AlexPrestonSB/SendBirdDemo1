package com.sendbirdsampleapp.data.preferences


interface PreferenceHelper {

    fun getUserId(): String

    fun setUserId(userId: String?)

    fun getNickname(): String

    fun setNickname(nickname: String?)

    fun getConnected(): Boolean

    fun setConnected(connected: Boolean)
}