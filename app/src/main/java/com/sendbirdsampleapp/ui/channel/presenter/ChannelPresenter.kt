package com.sendbirdsampleapp.ui.channel.presenter

import com.sendbirdsampleapp.ui.channel.view.ChannelView

interface ChannelPresenter {

    fun setView(view: ChannelView)

    fun navigateToGroupChannels()

    fun navigateToCalls()

    fun logoutPressed()
}
