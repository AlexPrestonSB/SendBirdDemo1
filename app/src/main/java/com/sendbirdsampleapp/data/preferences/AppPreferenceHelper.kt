package com.sendbirdsampleapp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppPreferenceHelper @Inject constructor(context: Context) :
    PreferenceHelper {

    companion object {
        private val SENDBIRD = "sendbird"
        private val PREFERENCE_KEY_USERID = "userId"
        private val PREFERENCE_KEY_NICKNAME = "nickname"
        private val PREFERENCE_KEY_CONNECTED = "connected"
        private val PREFERENCE_KEY_NOTIFICATIONS = "notifications"
        private val PREFERENCE_KEY_NOTIFICATIONS_SHOW_PREVIEWS = "notificationShowPreviews"
        private val PREFERENCE_KEY_NOTIFICATIONS_DO_NOT_DISTURB = "notificationsNoNotDisturb"
        private val PREFERENCE_KEY_GCM_TOKEN = "gcmToken"
        private val PREFERENCE_CALL_TOKEN = "callToken"
    }

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(SENDBIRD, Context.MODE_PRIVATE)

    override fun getUserId(): String =  mPrefs.getString(PREFERENCE_KEY_USERID, "UserId")!!

    override fun setUserId(userId: String?) { mPrefs.edit{ it.putString(PREFERENCE_KEY_USERID, userId)}}

    override fun getNickname(): String = mPrefs.getString(PREFERENCE_KEY_NICKNAME, "Nickname")!!

    override fun setNickname(nickname: String?) { mPrefs.edit { it.putString(PREFERENCE_KEY_NICKNAME, nickname) } }

    override fun getConnected(): Boolean =  mPrefs.getBoolean(PREFERENCE_KEY_CONNECTED, false)

    override fun setConnected(connected: Boolean) { mPrefs.edit{it.putBoolean(PREFERENCE_KEY_CONNECTED, connected)} }

    override fun getToken(): String =  mPrefs.getString(PREFERENCE_KEY_GCM_TOKEN, "token")!!

    override fun setToken(token: String?) { mPrefs.edit { it.putString(PREFERENCE_KEY_GCM_TOKEN, token) }}

    override fun setCallToken(token: String?) { mPrefs.edit { it.putString(PREFERENCE_CALL_TOKEN, token)} }

    override fun getCallToken() = mPrefs.getString(PREFERENCE_CALL_TOKEN, "callToken")

}


