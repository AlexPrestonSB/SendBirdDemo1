package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import android.content.Context
import android.net.Uri
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView

interface GroupChannelChatPresenter {

    fun setView(view: GroupChannelChatView)

    fun enterChannel(channelUrl: String)

    fun sendMessage(message: String)

    fun requestMedia()

    fun sendMessageThumbnail(context: Context, uri: Uri)

    fun backPressed()

    fun onResume()

    fun onPause()

    fun setTypingStatus(typing: Boolean)
}