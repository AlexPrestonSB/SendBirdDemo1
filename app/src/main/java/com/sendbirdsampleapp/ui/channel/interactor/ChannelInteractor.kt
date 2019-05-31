package com.sendbirdsampleapp.ui.channel.interactor

import com.sendbird.android.BaseChannel

class ChannelInteractor {

    interface OnChannelPressedListener {
        fun onGroupChannelPressed()
        fun onOpenChannelPressed()
        fun onError()
    }

    fun channelPressed(channelType: BaseChannel.ChannelType, listener: OnChannelPressedListener) {
        when {
            channelType.equals(BaseChannel.ChannelType.GROUP) -> listener.onGroupChannelPressed()
            channelType.equals(BaseChannel.ChannelType.OPEN) -> listener.onOpenChannelPressed()
            else -> listener.onError()
        }
    }

}