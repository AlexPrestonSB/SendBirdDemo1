package com.sendbirdsampleapp.ui.group_channel.chat_group.view

import com.sendbird.android.BaseMessage
import com.sendbird.android.Member
import com.sendbird.android.UserMessage

interface GroupChannelChatView {

    fun loadPreviousMessages(messages: MutableList<BaseMessage>)

    fun showValidationMessage(errorCode: Int)

    fun backPressed()

    fun sendMessage(message: UserMessage)

    fun receiveMessage(message: BaseMessage)

    fun typingIndicator(message: String)

    fun displayChatTitle(title: String)

}