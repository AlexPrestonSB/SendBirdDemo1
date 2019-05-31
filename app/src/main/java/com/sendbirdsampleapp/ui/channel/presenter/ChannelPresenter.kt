package com.sendbirdsampleapp.ui.channel.presenter

import com.sendbird.android.BaseChannel
import com.sendbirdsampleapp.ui.channel.interactor.ChannelInteractor
import com.sendbirdsampleapp.ui.channel.view.ChannelView

class ChannelPresenter(var channelView: ChannelView?, val channelInteractor: ChannelInteractor) :
    ChannelInteractor.OnChannelPressedListener {

    fun onChannelPressed(channelType: BaseChannel.ChannelType) {
        channelInteractor.channelPressed(channelType, this)
    }


    override fun onGroupChannelPressed() {
        channelView?.apply {
            navigateToGroupChannels()
        }
    }

    override fun onOpenChannelPressed() {
        channelView?.apply {
            navigateToOpenChannels()
        }
    }

    override fun onError() {

    }
}