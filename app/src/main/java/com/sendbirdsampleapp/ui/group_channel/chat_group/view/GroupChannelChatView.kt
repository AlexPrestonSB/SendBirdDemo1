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

    fun searchMessages(message: MutableList<BaseMessage>)

    //SyncManager below

    fun insert(messages: MutableList<BaseMessage>)

    fun update(messages: MutableList<BaseMessage>)

    fun remove(messages: MutableList<BaseMessage>)

    fun markAllRead()

    fun clear()


}