package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.sendbird.android.*
import com.sendbirdsampleapp.data.UrlInfo
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.util.AppConstants
import com.sendbirdsampleapp.util.FileUtil
import com.sendbirdsampleapp.util.UrlUtil
import java.io.File
import java.lang.Exception

class GroupChannelChatPresenterImpl : GroupChannelChatPresenter {

    private lateinit var view: GroupChannelChatView
    private lateinit var channel: GroupChannel
    private lateinit var channelUrl: String
    private lateinit var message: String

    override fun setView(view: GroupChannelChatView) {
        this.view = view
    }

    override fun enterChannel(channelUrl: String) {
        this.channelUrl = channelUrl
        Thread {
            GroupChannel.getChannel(channelUrl) { groupChannel, sendBirdException ->
                if (sendBirdException != null) {
                    view.showValidationMessage(AppConstants.FAILED_CHANNEL_GET)
                } else {

                    loadPreviousMessages(groupChannel)
                    view.displayChatTitle(groupChannel.name)
                    channel = groupChannel
                }
            }
        }.start()
    }

    override fun sendMessage(message: String) {

        val urls = UrlUtil.extractUrl(message)
        if (urls.size > 0) {
            sendMessageWithUrl(message, urls.get(0))
        } else {
            channel.sendUserMessage(message) { userMessage, sendBirdException ->
                if (sendBirdException != null) {
                    view.showValidationMessage(1)
                } else {
                    view.sendMessage(userMessage)
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

        if (!path.equals("")){
           val handler =  object : BaseChannel.SendFileMessageWithProgressHandler {
               override fun onSent(fileMessage: FileMessage?, exception: SendBirdException?) {
                   if (exception != null){
                       view.showValidationMessage(1)
                   } else {
                       view.sendMessage(fileMessage as BaseMessage)
                   }
               }

               override fun onProgress(bytesSent: Int, totalBytesSent: Int, totalBytesToSend: Int) {
                   //TODO when you have it
               }
           }
            channel.sendFileMessage(file, fileName, mime, size, "", null, thumbnailSize, handler)
        }



    }

    override fun onResume() {

        SendBird.addChannelHandler(AppConstants.CHANNEL_HANDLER_ID, object : SendBird.ChannelHandler() {
            override fun onMessageReceived(baseChannel: BaseChannel?, baseMessage: BaseMessage?) {
                if (baseChannel?.url.equals(channelUrl)) {
                    if (baseMessage != null) {
                        view.receiveMessage(baseMessage)
                    }
                } else {
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

    override fun setTypingStatus(typing: Boolean) {
        if (typing) {
            channel.startTyping()
        } else {
            channel.endTyping()
        }
    }

    fun sendMessageWithUrl(text: String, url: String) {
        message = text
        UrlUtil.generateLinkPreviewCallback(url, linkPreviewCallback)
    }

    val linkPreviewCallback = object : LinkPreviewCallback {
        override fun onPre() {

        }

        override fun onPos(sourceContent: SourceContent?, isNull: Boolean) {
            if (!isNull) {
                val urlInfo = UrlUtil.parseContent(sourceContent!!)

                channel.sendUserMessage(message, urlInfo.toJsonString(), "url_preview", null) { userMessage, e ->
                    if (e != null) {
                        view.showValidationMessage(1)
                    } else {
                        view.sendMessage(userMessage)
                    }
                }
            }

        }
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

    private fun loadPreviousMessages(groupChannel: GroupChannel) {
        Thread {
            val prevMessages = groupChannel.createPreviousMessageListQuery()
            prevMessages.load(100, true) { messages, sendBirdException ->
                if (sendBirdException != null) {
                    //TODO
                } else {
                    view.loadPreviousMessages(messages)
                }
            }
        }.start()
    }
}