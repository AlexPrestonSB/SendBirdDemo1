package com.sendbirdsampleapp.ui.group_channel.list_group.presenter

import android.util.Log
import com.sendbird.android.GroupChannel

import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView
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

    override fun onResume() {

    }

    override fun setUserChannels() {

        val channelListQuery = GroupChannel.createMyGroupChannelListQuery()
        channelListQuery.isIncludeEmpty = true
        channelListQuery.limit = 100

        channelListQuery.next() { channels, e ->
            if (e != null) {
                Log.e("TAG",e.printStackTrace().toString())
                view.showValidationMessage(AppConstants.FAILED_CHANNEL_GET)
            } else {
                view.setUserChannels(channels)
            }
        }
    }

}