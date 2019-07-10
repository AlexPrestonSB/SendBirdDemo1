package com.sendbirdsampleapp.ui.group_channel.list_group.presenter


import android.app.Activity
import android.content.Context
import com.sendbird.android.ConnectionManager
import com.sendbird.android.SendBird
import com.sendbird.syncmanager.SendBirdSyncManager
import com.sendbirdsampleapp.BaseApp
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView
import com.sendbirdsampleapp.util.AppConstants
import com.sendbirdsampleapp.util.ConnectionUtil
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

        SendBirdSyncManager.setup(context, userId) {
            if (it != null) {
                view.showValidationMessage(it.code)
            } else {
                (context as BaseApp).setSyncManagerSetUp(true)
                (context as Activity).runOnUiThread() {
                    if (!SendBird.getConnectionState().equals(SendBird.ConnectionState.OPEN)) {
                        //refresh()
                    }
                    ConnectionUtil.addConnectionManagementHandler(AppConstants.CONNECTION_HANDLER_ID, userId, object: ConnectionUtil.ConnectionManagementHandler{
                        override fun onConnected(connected: Boolean) {
                            //refresh()
                        }
                    })
                }
            }
        }
    }
}