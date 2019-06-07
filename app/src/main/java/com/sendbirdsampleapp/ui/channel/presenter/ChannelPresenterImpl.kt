package com.sendbirdsampleapp.ui.channel.presenter

import com.sendbirdsampleapp.ui.channel.view.ChannelView

class ChannelPresenterImpl : ChannelPresenter {

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
}