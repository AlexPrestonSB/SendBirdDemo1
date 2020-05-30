package com.sendbirdsampleapp.ui.channel.view

interface ChannelView {

    fun navigateToGroupChannels()

    fun showValidationMessage(errorCode: Int)

    fun logoutPressed()

    fun navigateToCalls()

}