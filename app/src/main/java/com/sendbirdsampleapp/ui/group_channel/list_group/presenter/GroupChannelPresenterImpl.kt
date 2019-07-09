package com.sendbirdsampleapp.ui.group_channel.list_group.presenter


import android.content.Context
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView
import javax.inject.Inject

class GroupChannelPresenterImpl @Inject constructor(private val preferenceHelper: AppPreferenceHelper): GroupChannelPresenter {

    private lateinit var view: GroupChannelView

    override fun setView(groupView: GroupChannelView) {
        this.view = groupView
    }

    override fun createGroupPressed() {
        view.createGroupPressed()
    }

    override fun backPressed() {
        view.backPressed()
    }

    override fun onResume(context: Context) {
        val userId = preferenceHelper.getUserId()
    }
}