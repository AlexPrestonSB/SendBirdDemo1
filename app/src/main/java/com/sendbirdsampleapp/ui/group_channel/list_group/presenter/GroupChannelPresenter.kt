package com.sendbirdsampleapp.ui.group_channel.list_group.presenter

import android.content.Context
import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView

interface GroupChannelPresenter {

    fun setView(groupView: GroupChannelView)

    fun createGroupPressed()

    fun backPressed()

    fun setUpRecyclerView()

    fun clearSearch()

    fun searchMessages(word: String)

    fun onResume(context: Context)

    fun onPause()

}