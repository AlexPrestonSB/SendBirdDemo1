package com.sendbirdsampleapp.ui.group_channel.list_group.presenter


import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView

class GroupChannelPresenterImpl: GroupChannelPresenter {

    private lateinit var view: GroupChannelView

    override fun setView(groupView: GroupChannelView) {
        this.view = groupView
    }

    override fun createGroupPressed() {
        view.createGroupPressed()
    }

}