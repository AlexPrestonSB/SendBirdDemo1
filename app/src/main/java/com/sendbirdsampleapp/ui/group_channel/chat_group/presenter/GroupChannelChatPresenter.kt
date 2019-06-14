package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import android.net.Uri
import com.sendbird.android.GroupChannel
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import java.net.URI

interface GroupChannelChatPresenter {

    fun setView(view: GroupChannelChatView)

    fun enterChannel(channelUrl: String)

    fun sendMessage(message: String)

    fun requestMedia()

    fun sendMessageThumbnail(uri: Uri?)

    fun backPressed()

    fun onResume()

    fun onPause()

    fun setTypingStatus(typing: Boolean)
}