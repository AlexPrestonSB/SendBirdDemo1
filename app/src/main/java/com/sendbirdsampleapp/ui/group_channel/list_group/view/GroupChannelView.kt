package com.sendbirdsampleapp.ui.group_channel.list_group.view

import com.sendbird.android.GroupChannel
import com.sendbird.android.GroupChannelListQuery


interface GroupChannelView {

    fun showValidationMessage(errorCode: Int)

    fun createGroupPressed()

    fun backPressed()

    fun setUserChannels(channels: MutableList<GroupChannel>)

    fun updateChannels(channels: MutableList<GroupChannel>)

    fun removeChannels(channels: MutableList<GroupChannel>)

    fun insertChannels(channels: MutableList<GroupChannel>, order: GroupChannelListQuery.Order)

    fun moveChannels(channels: MutableList<GroupChannel>, order: GroupChannelListQuery.Order)

    fun clearChannels()


}