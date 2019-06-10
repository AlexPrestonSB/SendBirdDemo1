package com.sendbirdsampleapp.ui.group_channel.create_group.presenter

import com.sendbird.android.GroupChannel
import com.sendbirdsampleapp.ui.group_channel.create_group.view.GroupChannelCreateView
import com.sendbirdsampleapp.util.AppConstants

class GroupChannelCreatePresenterImpl : GroupChannelCreatePresenter {

    private lateinit var view: GroupChannelCreateView

    override fun setView(groupView: GroupChannelCreateView) {
        this.view = groupView
    }

    override fun createChannel(users: MutableList<String>) {
        GroupChannel.createChannelWithUserIds(users, true) { groupChannel, sendBirdException ->
            if (sendBirdException != null) {
                view.showValidationMessage(AppConstants.FAILED_CHANNEL_CREATE)
            } else {
                val test = groupChannel
                view.createChannelPressed(groupChannel.url)
            }
        }
    }
}