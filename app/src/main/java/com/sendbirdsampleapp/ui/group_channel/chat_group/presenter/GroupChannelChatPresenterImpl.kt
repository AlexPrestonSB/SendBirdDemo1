package com.sendbirdsampleapp.ui.group_channel.chat_group.presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.view.View
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.sendbird.android.*
import com.sendbird.syncmanager.*
import com.sendbird.syncmanager.handler.MessageCollectionHandler
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.util.AppConstants
import com.sendbirdsampleapp.util.ConnectionUtil
import com.sendbirdsampleapp.util.FileUtil
import com.sendbirdsampleapp.util.UrlUtil
import java.io.File
import javax.inject.Inject


class GroupChannelChatPresenterImpl @Inject constructor(private val preferenceHelper: AppPreferenceHelper) :
    GroupChannelChatPresenter {

    private lateinit var view: GroupChannelChatView
    private lateinit var channelUrl: String
    private lateinit var message: String
    private lateinit var context: Context

    private var channel: GroupChannel? = null

    private var messageCollection: MessageCollection? = null
    private val messageFilter = MessageFilter(BaseChannel.MessageTypeFilter.ALL, null, null)

    override fun setView(view: GroupChannelChatView) {
        this.view = view
    }

    //Refactor only need one send message
    override fun sendMessage(message: String) {
        val urls = UrlUtil.extractUrl(message)
        if (urls.size > 0) {
            sendMessageWithUrl(message, urls.get(0))
        } else {
            channel!!.sendUserMessage(message) { userMessage, sendBirdException ->
                if (sendBirdException != null) {
                    view.showValidationMessage(1)
                } else {
                    messageCollection?.appendMessage(userMessage as BaseMessage)
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
                        messageCollection?.appendMessage(fileMessage as BaseMessage)

                    }
                }

                override fun onProgress(
                    bytesSent: Int,
                    totalBytesSent: Int,
                    totalBytesToSend: Int
                ) {
                    //TODO when you have it
                }
            }
            channel!!.sendFileMessage(file, fileName, mime, size, "", null, thumbnailSize, handler)
        }

    }

    override fun onResume(context: Context, channelUrl: String) {

        this.context = context
        val userId = preferenceHelper.getUserId()
        this.channelUrl = channelUrl

        SendBirdSyncManager.setup(context, userId) {

            (context as Activity).runOnUiThread {
                if (!SendBird.getConnectionState().equals(SendBird.ConnectionState.OPEN)) {
                    refresh()
                }
                createMessageCollection(channelUrl)
                ConnectionUtil.addConnectionManagementHandler(
                    AppConstants.CONNECTION_HANDLER_ID,
                    userId,
                    object : ConnectionUtil.ConnectionManagementHandler {
                        override fun onConnected(connected: Boolean) {
                            refresh()
                        }
                    })
            }

        }

        SendBird.addChannelHandler(
            AppConstants.CHANNEL_HANDLER_ID,
            object : SendBird.ChannelHandler() {
                override fun onMessageReceived(
                    baseChannel: BaseChannel?,
                    baseMessage: BaseMessage?
                ) {
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
        if (channel != null) {
            channel!!.refresh {
                if (it != null) {
                    it.printStackTrace() // ADD MORE error handling
                    return@refresh
                }
                //TODO update UI components
            }

            if (messageCollection != null) {
                messageCollection!!.fetch(MessageCollection.Direction.NEXT, null)
            }

        } else {
            createMessageCollection(channelUrl)
        }


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

    @SuppressLint("MissingPermission") //Should obviously implicitly check permissions and not assume they have been given. (Declared in manifest for test sake)
    override fun shareLocation(context: Context) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val longitude = location.longitude
        val latitude = location.latitude

        val result = "$longitude,$latitude"
        //TODO
        val params = UserMessageParams()
        params.setMessage(result)
        params.setCustomType("location")

        if (channel != null) {
            channel!!.sendUserMessage(params) { userMessage, e ->
                if (e != null) {
                    view.showValidationMessage(1)
                } else {
                    messageCollection?.appendMessage(userMessage as BaseMessage)
                }

            }
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
                    channel!!.sendUserMessage(
                        message,
                        urlInfo.toJsonString(),
                        "url_preview",
                        null
                    ) { userMessage, e ->
                        if (e != null) {
                            view.showValidationMessage(1)
                        } else {

                            messageCollection?.appendMessage(userMessage as BaseMessage)

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

    private fun createMessageCollection(channelUrl: String) {
        if (SendBird.getConnectionState() != SendBird.ConnectionState.OPEN) {
            MessageCollection.create(channelUrl, messageFilter, Long.MAX_VALUE) { collection, e ->
                if (e == null) {
                    if (messageCollection == null) {
                        messageCollection = collection
                        messageCollection?.setCollectionHandler(messageCollectionHandler)
                        channel = messageCollection?.channel

                        messageCollection?.fetch(MessageCollection.Direction.PREVIOUS) {
                            if (it != null) {
                                return@fetch
                            }

                            (context as Activity).runOnUiThread() {
                                view.markAllRead()
                            }
                        }
                    }
                }
            }
        } else {
            GroupChannel.getChannel(channelUrl) { groupChannel, e ->
                if (e == null) {
                    channel = groupChannel

                    if (messageCollection == null) {
                        messageCollection =
                            MessageCollection(groupChannel, messageFilter, Long.MAX_VALUE)
                        messageCollection?.setCollectionHandler(messageCollectionHandler)

                        messageCollection?.fetchSucceededMessages(
                            MessageCollection.Direction.PREVIOUS
                        ) { hasMore, exception ->

                            (context as Activity).runOnUiThread() {
                                view.markAllRead()
                            }
                        }
                    }
                }

            }
        }
    }

    private val messageCollectionHandler: MessageCollectionHandler =
        object : MessageCollectionHandler() {
            override fun onMessageEvent(
                collection: MessageCollection,
                messages: List<BaseMessage>,
                action: MessageEventAction
            ) {
            }

            override fun onSucceededMessageEvent(
                collection: MessageCollection,
                messages: List<BaseMessage>,
                action: MessageEventAction
            ) {
                Log.d(
                    "SyncManager",
                    "onSucceededMessageEvent: size = " + messages.size + ", action = " + action
                )
//                if ((context as Activity) == null) {
//                    return
//                }
                (context as Activity).runOnUiThread() {
                    when (action) {
                        MessageEventAction.INSERT -> {
                            val title =
                                channel!!.members[0].nickname + ", " + channel!!.members[1].nickname + "..."
                            view.displayChatTitle(title)
                            view.insert(messages as MutableList<BaseMessage>)
                            view.markAllRead()
                        }
                        MessageEventAction.REMOVE -> {
                            view.remove(messages as MutableList<BaseMessage>)

                        }
                        MessageEventAction.UPDATE -> {
                            view.update(messages as MutableList<BaseMessage>)

                        }
                        MessageEventAction.CLEAR -> {
                            view.clear()
                        }
                        else -> {
                            view.showValidationMessage(1)
                        }
                    }
                }
            }

            override fun onPendingMessageEvent(
                collection: MessageCollection,
                messages: List<BaseMessage>,
                action: MessageEventAction
            ) {
                Log.d(
                    "SyncManager",
                    "onPendingMessageEvent: size = " + messages.size + ", action = " + action
                )
                // TODO implement pending message event
            }

            override fun onFailedMessageEvent(
                collection: MessageCollection,
                messages: List<BaseMessage>,
                action: MessageEventAction,
                reason: FailedMessageEventActionReason
            ) {
                Log.d(
                    "SyncManager",
                    "onFailedMessageEvent: size = " + messages.size + ", action = " + action
                )
                    // TODO implement failed message event
            }

            override fun onNewMessage(
                collection: MessageCollection,
                message: BaseMessage
            ) {
                Log.d("SyncManager", "onNewMessage: message = $message")
                //show when the scroll position is bottom ONLY.
            }
        }

// OLD CODE
//    private val messageCollectionHandler = MessageCollectionHandler { collection, messages, action ->
//        Log.d("SyncManager", "onMessageEvent: size = " + messages.size + ", action = " + action)
//
//        (context as Activity).runOnUiThread() {
//            when (action) {
//                MessageEventAction.INSERT -> {
//                    val title = channel!!.members[0].nickname + ", " +  channel!!.members[1].nickname + "..."
//                    view.displayChatTitle(title)
//                    view.insert(messages)
//                    view.markAllRead()
//                }
//                MessageEventAction.REMOVE -> {
//                    view.remove(messages)
//
//                }
//                MessageEventAction.UPDATE -> {
//                    view.update(messages)
//
//                }
//                MessageEventAction.CLEAR -> {
//                    view.clear()
//                }
//                else -> {
//                    view.showValidationMessage(1)
//                }
//            }
//        }
//
//    }
}