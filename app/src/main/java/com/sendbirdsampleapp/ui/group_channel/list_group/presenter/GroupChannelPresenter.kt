package com.sendbirdsampleapp.ui.group_channel.list_group.presenter

import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView

interface GroupChannelPresenter {

    fun setView(groupView: GroupChannelView)

    fun createGroupPressed()

}