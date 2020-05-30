package com.sendbirdsampleapp.ui.channel.presenter

import com.sendbird.android.SendBird
import com.sendbird.syncmanager.SendBirdSyncManager
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.channel.view.ChannelView
import com.sendbirdsampleapp.util.PushUtil
import javax.inject.Inject

class ChannelPresenterImpl@Inject constructor(private val preferenceHelper: AppPreferenceHelper) : ChannelPresenter {

    private lateinit var channelView: ChannelView


    override fun setView(view: ChannelView) {
        this.channelView = view
    }

    override fun navigateToGroupChannels() {
        channelView.navigateToGroupChannels()
    }
    override fun logoutPressed() {
        SendBird.disconnect {
            PushUtil.unregisterPushTokenForCurrentUser(preferenceHelper.getToken(), null)
            channelView.logoutPressed()
            preferenceHelper.setConnected(false)
        }
    }

    override fun navigateToCalls() {
        channelView.navigateToCalls()
    }
}