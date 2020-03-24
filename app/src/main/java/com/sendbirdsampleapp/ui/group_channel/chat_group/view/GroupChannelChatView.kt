package com.sendbirdsampleapp.ui.group_channel.chat_group.view

import android.content.Intent
import com.sendbird.android.BaseMessage
import com.sendbird.android.GroupChannel
import com.sendbird.android.Member
import com.sendbird.android.UserMessage

interface GroupChannelChatView {

    fun showValidationMessage(errorCode: Int)

    fun backPressed()

    fun typingIndicator(message: String)

    fun displayChatTitle(title: String)

    fun displayPushNotification(message: UserMessage, channelUrl: String?)

    fun selectMedia(intent: Intent)

    fun searchMessages(messages: MutableList<BaseMessage>)

    fun addFirst(message: BaseMessage)

    fun loadMessages(messages: MutableList<BaseMessage>)

    fun clear()


}