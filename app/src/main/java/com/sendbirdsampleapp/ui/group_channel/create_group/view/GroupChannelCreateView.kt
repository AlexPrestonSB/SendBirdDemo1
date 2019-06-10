package com.sendbirdsampleapp.ui.group_channel.create_group.view

interface GroupChannelCreateView {

    fun createChannelPressed(channelId: String)

    fun showValidationMessage(errorCode: Int)

    fun backPressed()


}