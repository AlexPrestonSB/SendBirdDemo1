package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import com.sendbird.android.*
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.util.AppConstants

class GroupChannelChatPresenterImpl: GroupChannelChatPresenter {

    private lateinit var view: GroupChannelChatView
    private lateinit var channel: GroupChannel
    private lateinit var channelUrl: String

    override fun setView(view: GroupChannelChatView) {
        this.view = view
    }

    override fun enterChannel(channelUrl: String) {
        this.channelUrl = channelUrl
        Thread {
            GroupChannel.getChannel(channelUrl) {groupChannel, sendBirdException ->
                if (sendBirdException != null) {
                    view.showValidationMessage(AppConstants.FAILED_CHANNEL_GET)
                } else {
                    loadPreviousMessages(groupChannel)
                    channel = groupChannel
                }
            }
        }.start()
    }

    override fun sendMessage(message: String) {

        val tempMessage = channel.sendUserMessage(message) { userMessage, sendBirdException ->
            if  (sendBirdException != null) {
                //TODO handle error
            } else {
                view.sendMessage(userMessage)
            }
        }
    }

    override fun backPressed() {
        view.backPressed()
    }

    override fun uploadImage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {

        SendBird.addChannelHandler(AppConstants.CHANNEL_HANDLER_ID, object: SendBird.ChannelHandler() {
            override fun onMessageReceived(baseChannel: BaseChannel?, baseMessage: BaseMessage?) {
                if (baseChannel?.url.equals(channelUrl)) {
                    if (baseMessage != null) {
                        view.receiveMessage(baseMessage)
                    }
                }
            }

            override fun onTypingStatusUpdated(channel: GroupChannel?) {
                if (channel?.url.equals(channelUrl)) {
                    val typingUsers = channel?.typingMembers
                    view.typingIndicator(preparedMessage(typingUsers as MutableList<Member>))
                }
            }
        })


    }

    fun preparedMessage(users: MutableList<Member>): String {
        when (users.size) {
            0 -> return ""
            1 -> {
                return users.get(0).nickname + " is typing.."
            }
            2 -> {
                return users.get(0).nickname + " " + users.get(1) + " are typing.."
            }
            else -> return "Multiple users are typing.."
        }
    }

    fun loadPreviousMessages(groupChannel: GroupChannel) {
        Thread {
            val prevMessages = groupChannel.createPreviousMessageListQuery()
            prevMessages.load(100,true) { messages, sendBirdException ->
                if (sendBirdException!= null){
                    //TODO
                } else {
                    view.loadPreviousMessages(messages)
                }
            }
        }.start()
    }
}