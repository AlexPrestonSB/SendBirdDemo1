package com.sendbirdsampleapp.ui.channel.presenter

import com.sendbird.android.SendBird
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.channel.view.ChannelView
import javax.inject.Inject

class ChannelPresenterImpl@Inject constructor(private val preferenceHelper: AppPreferenceHelper) : ChannelPresenter {

    private lateinit var channelView: ChannelView


    override fun setView(view: ChannelView) {
        this.channelView = view
    }

    override fun navigateToGroupChannels() {
        channelView.navigateToGroupChannels()
    }

    override fun navigateToOpenChannels() {
        channelView.navigateToOpenChannels()
    }

    override fun logoutPressed() {
        SendBird.disconnect {
            channelView.logoutPressed()
            preferenceHelper.setConnected(false)
        }
    }
}