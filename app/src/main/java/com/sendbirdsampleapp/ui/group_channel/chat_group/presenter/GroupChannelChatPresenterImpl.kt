package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.sendbird.android.*
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.util.*
import java.io.File
import javax.inject.Inject

class GroupChannelChatPresenterImpl @Inject constructor(private val preferenceHelper: AppPreferenceHelper) :
    GroupChannelChatPresenter {

    private lateinit var view: GroupChannelChatView
    private lateinit var channelUrl: String
    private lateinit var message: String
    private lateinit var context: Context

    private var channel: GroupChannel? = null

    override fun setView(view: GroupChannelChatView) {
        this.view = view
    }

    override fun sendMessage(message: String) {
        val urls = UrlUtil.extractUrl(message)
        if (urls.size > 0) {
            sendMessageWithUrl(message, urls.get(0))
        } else {
            channel!!.sendUserMessage(message) { userMessage, sendBirdException ->
                if (sendBirdException != null) {
                    view.showValidationMessage(1)
                } else {
                    view.addFirst(userMessage as BaseMessage)
                }
            }
        }
    }

    override fun backPressed() {
        view.backPressed()
    }

    override fun requestMedia() {
        val intent = Intent()
        intent.type = "*/*"
        val mimeTypes = arrayOf("image/*", "video/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setAction(Intent.ACTION_GET_CONTENT)

        view.selectMedia(intent)

    }

    override fun sendMessageThumbnail(context: Context, uri: Uri) {
        val thumbnailSize = ArrayList<FileMessage.ThumbnailSize>()
        thumbnailSize.add(FileMessage.ThumbnailSize(240, 240))
        thumbnailSize.add(FileMessage.ThumbnailSize(320, 320))

        val info = FileUtil.getFileInfo(context, uri)

        if (info == null) {
            view.showValidationMessage(101) //TODO CHANGE
        }

        val path = info?.get("path") as String
        val file = File(path)
        val fileName = file.name
        val mime = info.get("mime") as String
        val size = info.get("size") as Int

        if (!path.equals("")) {
            val handler = object : BaseChannel.SendFileMessageWithProgressHandler {
                override fun onSent(fileMessage: FileMessage?, exception: SendBirdException?) {
                    if (exception != null) {
                        view.showValidationMessage(1)
                    } else {
                        //Send Message
                        view.addFirst(fileMessage as BaseMessage)
                    }
                }

                override fun onProgress(bytesSent: Int, totalBytesSent: Int, totalBytesToSend: Int) {
                    //TODO when you have it
                }
            }
            channel!!.sendFileMessage(file, fileName, mime, size, "", null, thumbnailSize, handler)
        }

    }

    override fun onResume(context: Context, channelUrl: String) {

        this.context = context
        this.channelUrl = channelUrl

        GroupChannel.getChannel(channelUrl) {groupChannel, sendBirdException ->
            if (sendBirdException != null) {
                Log.e("TAG", sendBirdException.toString())
            }
            this.channel = groupChannel
            loadMessages()
        }

        SendBird.addChannelHandler(AppConstants.CHANNEL_HANDLER_ID, object : SendBird.ChannelHandler() {
            override fun onMessageReceived(baseChannel: BaseChannel?, baseMessage: BaseMessage?) {
                if (!baseChannel?.url.equals(channelUrl)) {
                    view.displayPushNotification(baseMessage as UserMessage, baseChannel?.url)
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


    override fun onPause() {
        SendBird.removeChannelHandler(AppConstants.CHANNEL_HANDLER_ID)

    }

    override fun refresh() {

    }

    override fun setTypingStatus(typing: Boolean) {
        if (channel == null) {
            return
        }

        if (typing) {
            channel!!.startTyping()
        } else {
            channel!!.endTyping()
        }
    }

    override fun loadMessages() {
        val query = channel!!.createPreviousMessageListQuery()

        query.load(30, true) { messages, sendBirdException ->
            if (sendBirdException != null) {
                Log.e("TAG", sendBirdException.toString())
            }
            view.loadMessages(messages)
        }

    }

    override fun clear() {
        view.clear()
        loadMessages()
    }

    override fun messageSearch(word: String) {

        val query = MessageSearchQuery.Builder().setKeyword(word).setChannelUrl(channelUrl).build()

        query.next { messages, sendBirdException ->
            if (sendBirdException != null) {
                Log.e("ERROR", sendBirdException.message)
            }
            getSearchedMessage(messages)
        }
    }

    private fun getSearchedMessage(results: MutableList<BaseMessage>) {

        channel!!.getNextMessagesById(results[0].messageId, true, 30, true, BaseChannel.MessageTypeFilter.ALL, "") { messages, sendBirdException ->

            if (sendBirdException != null) {
                Log.e("TAG", sendBirdException.toString())
            }

            view.loadMessages(messages)
        }
    }


    private fun sendMessageWithUrl(text: String, url: String) {
        message = text
        UrlUtil.generateLinkPreviewCallback(url, linkPreviewCallback)
    }

    private val linkPreviewCallback = object : LinkPreviewCallback {
        override fun onPre() {

        }

        override fun onPos(sourceContent: SourceContent?, isNull: Boolean) {
            if (!isNull) {
                val urlInfo = UrlUtil.parseContent(sourceContent!!)
                if (channel != null) {
                    channel!!.sendUserMessage(message, urlInfo.toJsonString(), "url_preview", null) { userMessage, e ->
                        if (e != null) {
                            view.showValidationMessage(1)
                        } else {

                            view.addFirst(userMessage as BaseMessage)

                        }
                    }
                }
            }

        }
    }

    fun preparedMessage(users: MutableList<Member>): String {
        return when (users.size) {
            0 -> ""
            1 -> {
                users.get(0).nickname + " is typing.."
            }
            2 -> {
                users.get(0).nickname + " " + users.get(1) + " are typing.."
            }
            else -> "Multiple users are typing.."
        }
    }

}