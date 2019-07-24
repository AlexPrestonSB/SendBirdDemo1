package com.sendbirdsampleapp.util

import com.sendbird.android.*

object SyncManagerUtil {


    fun findIndexOfMessage(messages: MutableList<BaseMessage>, newMessage: BaseMessage): Int {

        if (messages.size == 0){
            return 0
        }

        if (messages.get(0).createdAt < newMessage.createdAt) {
            return 0
        }

        for (i in 0 until messages.size - 1) {
            val m1 = messages[i]
            val m2 = messages[i + 1]

            if (m1.createdAt > newMessage.createdAt && newMessage.createdAt > m2.createdAt) {
                return i + 1
            }
        }

        return messages.size
    }

    fun getIndexOfMessage(messages: List<BaseMessage>, targetMessage: BaseMessage): Int {
        for (i in messages.indices) {
            val msgId1 = messages[i].messageId
            val msgId2 = targetMessage.messageId

            if (msgId1 == msgId2) {
                if (msgId1 == 0L) {
                    if (getRequestId(messages[i]) == getRequestId(targetMessage)) {
                        return i
                    }
                } else {
                    return i
                }
            }
        }

        return -1
    }

    private fun getRequestId(message: BaseMessage): String {
        if (message is AdminMessage) {
            return ""
        }

        if (message is UserMessage) {
            return message.requestId
        }

        return if (message is FileMessage) {
            message.requestId
        } else ""

    }

    fun compareTo(c1: GroupChannel, c2: GroupChannel, order: GroupChannelListQuery.Order): Int {
        when (order) {
            GroupChannelListQuery.Order.CHRONOLOGICAL -> return if (c1.createdAt > c2.createdAt) {
                -1
            } else if (c1.createdAt < c2.createdAt) {
                1
            } else {
                0
            }
            GroupChannelListQuery.Order.LATEST_LAST_MESSAGE -> {
                val m1 = c1.lastMessage
                val m2 = c2.lastMessage

                val createdAt1 = m1?.createdAt ?: c1.createdAt
                val createdAt2 = m2?.createdAt ?: c2.createdAt

                return if (createdAt1 > createdAt2) {
                    -1
                } else if (createdAt1 < createdAt2) {
                    1
                } else {
                    0
                }
            }
            GroupChannelListQuery.Order.CHANNEL_NAME_ALPHABETICAL -> return c1.name.compareTo(c2.name)

            GroupChannelListQuery.Order.METADATA_VALUE_ALPHABETICAL -> {
            }
        }

        return 0
    }

    fun getIndexOfChannel(channels: List<GroupChannel>, targetChannel: GroupChannel): Int {
        for (i in channels.indices) {
            if (channels[i].url == targetChannel.url) {
                return i
            }
        }

        return -1
    }

    fun findIndexOfChannel(channels: List<GroupChannel>, targetChannel: GroupChannel, order: GroupChannelListQuery.Order): Int {
        if (channels.size == 0) {
            return 0
        }

        val index = channels.size
        for (i in channels.indices) {
            val c = channels[i]
            if (c.url == targetChannel.url) {
                return i
            }

            if (compareTo(targetChannel, c, order) < 0) {
                return i
            }
        }

        return index
    }
}