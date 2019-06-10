package com.sendbirdsampleapp.ui.group_channel.create_group.presenter

import com.sendbirdsampleapp.ui.group_channel.create_group.view.GroupChannelCreateView

interface GroupChannelCreatePresenter {

    fun setView(groupView: GroupChannelCreateView)


    fun createChannel(users: MutableList<String>)

}