package com.sendbirdsampleapp.ui.group_channel.presenter

import com.sendbird.android.GroupChannel

import com.sendbirdsampleapp.ui.group_channel.view.GroupChannelView
import com.sendbirdsampleapp.util.AppConstants

class GroupChannelPresenterImpl: GroupChannelPresenter {

    private lateinit var view: GroupChannelView

    override fun setView(groupView: GroupChannelView) {
        this.view = groupView
    }

    override fun createGroupPressed(users: List<String>) {
        GroupChannel.createChannelWithUserIds(users,true) { groupChannel, sendBirdException ->
            if (sendBirdException != null) {
                view.showValidationMessage(AppConstants.FAILED_GROUP_CREATE)
            }
        }
    }

    override fun getUserChannels() {

    }
}