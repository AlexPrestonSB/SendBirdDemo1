package com.sendbirdsampleapp.ui.group_channel.create_group.presenter

import com.sendbird.android.GroupChannel
import com.sendbird.android.GroupChannelParams
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.ui.group_channel.create_group.view.GroupChannelCreateView
import com.sendbirdsampleapp.util.AppConstants

class GroupChannelCreatePresenterImpl : GroupChannelCreatePresenter {

    private lateinit var view: GroupChannelCreateView

    override fun setView(groupView: GroupChannelCreateView) {
        this.view = groupView
    }

    override fun createChannel(users: MutableList<String>) {
        val params = GroupChannelParams()

        val operatorId = ArrayList<String>()
        operatorId.add(SendBird.getCurrentUser().userId)

        params.addUserIds(users)
        params.setOperatorUserIds(operatorId)

        GroupChannel.createChannel(params) { groupChannel, sendBirdException ->
            if (sendBirdException != null) {
                view.showValidationMessage(AppConstants.FAILED_CHANNEL_CREATE)
            } else {
                val test = groupChannel
                view.createChannelPressed(groupChannel.url)
            }
        }
    }

    override fun backPressed() {
        view.backPressed()
    }
}