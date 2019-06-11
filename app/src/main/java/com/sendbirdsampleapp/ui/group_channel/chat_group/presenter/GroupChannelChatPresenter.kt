package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView

interface GroupChannelChatPresenter {

    fun setView(view: GroupChannelChatView)

    fun enterChannel(channelUrl: String)

    fun sendMessage()

    fun uploadImage()
}