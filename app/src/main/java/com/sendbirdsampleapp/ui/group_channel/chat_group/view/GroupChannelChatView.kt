package com.sendbirdsampleapp.ui.group_channel.chat_group.view

import com.sendbird.android.BaseMessage

interface GroupChannelChatView {

    fun loadPreviousMessages(messages: MutableList<BaseMessage>)

    fun showValidationMessage(errorCode: Int)

    fun backPressed()

}