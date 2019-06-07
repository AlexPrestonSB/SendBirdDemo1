package com.sendbirdsampleapp.ui.channel.view

interface ChannelView {

    fun navigateToGroupChannels()

    fun navigateToOpenChannels()

    fun showValidationMessage(errorCode: Int)

}