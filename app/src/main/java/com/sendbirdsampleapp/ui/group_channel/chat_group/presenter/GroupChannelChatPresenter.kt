package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import com.sendbird.android.GroupChannel
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView

interface GroupChannelChatPresenter {

    fun setView(view: GroupChannelChatView)

    fun enterChannel(channelUrl: String)

    fun sendMessage(message: String)

    fun uploadImage()

    fun backPressed()

    fun onResume()

    fun onPause()

    fun setTypingStatus(typing: Boolean)
}