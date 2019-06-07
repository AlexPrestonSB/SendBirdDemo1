package com.sendbirdsampleapp.ui.group_channel.presenter

import com.sendbirdsampleapp.ui.group_channel.view.GroupChannelView

interface GroupChannelPresenter {

    fun setView(groupView: GroupChannelView)

    fun createGroupPressed(users: List<String>)

    fun getUserChannels()
}