package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import com.sendbird.android.BaseMessage
import com.sendbird.android.GroupChannel
import com.sendbird.android.PreviousMessageListQuery
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.util.AppConstants

class GroupChannelChatPresenterImpl: GroupChannelChatPresenter {

    private lateinit var view: GroupChannelChatView

    override fun setView(view: GroupChannelChatView) {
        this.view = view
    }

    override fun enterChannel(channelUrl: String) {
        Thread {
            GroupChannel.getChannel(channelUrl) {groupChannel, sendBirdException ->
                if (sendBirdException != null) {
                    view.showValidationMessage(AppConstants.FAILED_CHANNEL_GET)
                } else {
                    loadPreviousMessages(groupChannel)
                }
            }
        }.start()
    }

    override fun sendMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun backPressed() {
        view.backPressed()
    }

    override fun uploadImage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loadPreviousMessages(groupChannel: GroupChannel) {
        Thread {
            val prevMessages = groupChannel.createPreviousMessageListQuery()
            prevMessages.load(30,true) { messages, sendBirdException ->
                if (sendBirdException!= null){
                    //TODO
                } else {
                    view.loadPreviousMessages(messages)
                }
            }
        }.start()
    }
}