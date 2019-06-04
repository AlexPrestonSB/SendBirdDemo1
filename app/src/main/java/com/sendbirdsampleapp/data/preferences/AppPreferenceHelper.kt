package com.sendbirdsampleapp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.sendbirdsampleapp.di.PreferenceInfo
import javax.inject.Inject

class AppPreferenceHelper @Inject constructor(context: Context, @PreferenceInfo private val prefFileName: String) : PreferenceHelper {

    companion object {
        private val SENDBIRD = "sendbird"
        private val PREFERENCE_KEY_USERID = "userId"
        private val PREFERENCE_KEY_NICKNAME = "nickname"
        private val PREFERENCE_KEY_CONNECTED = "connected"
        private val PREFERENCE_KEY_NOTIFICATIONS = "notifications"
        private val PREFERENCE_KEY_NOTIFICATIONS_SHOW_PREVIEWS = "notificationShowPreviews"
        private val PREFERENCE_KEY_NOTIFICATIONS_DO_NOT_DISTURB = "notificationsNoNotDisturb"
    }

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getUserId(): String =  mPrefs.getString(PREFERENCE_KEY_USERID, "UserId")!!

    override fun setUserId(userId: String?) { mPrefs.edit{ it.putString(PREFERENCE_KEY_NICKNAME, userId)}}

    override fun getNickname(): String = mPrefs.getString(PREFERENCE_KEY_NICKNAME, "Nickname")!!

    override fun setNickname(nickname: String?) { mPrefs.edit { it.putString(PREFERENCE_KEY_NICKNAME, nickname) } }

    override fun getConnected(): Boolean =  mPrefs.getBoolean(PREFERENCE_KEY_CONNECTED, true)

    override fun setConnected(connected: Boolean) { mPrefs.edit{it.putBoolean(PREFERENCE_KEY_CONNECTED, connected)} }
}

