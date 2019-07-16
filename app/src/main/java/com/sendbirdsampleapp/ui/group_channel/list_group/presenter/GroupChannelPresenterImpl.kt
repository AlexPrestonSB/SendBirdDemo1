package com.sendbirdsampleapp.ui.group_channel.list_group.presenter


import android.app.Activity
import android.content.Context
import android.util.Log
import com.sendbird.android.ConnectionManager
import com.sendbird.android.GroupChannel
import com.sendbird.android.SendBird
import com.sendbird.android.SendBirdException
import com.sendbird.syncmanager.ChannelCollection
import com.sendbird.syncmanager.ChannelEventAction
import com.sendbird.syncmanager.SendBirdSyncManager
import com.sendbird.syncmanager.handler.ChannelCollectionHandler
import com.sendbird.syncmanager.handler.CompletionHandler
import com.sendbirdsampleapp.BaseApp
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView
import com.sendbirdsampleapp.util.AppConstants
import com.sendbirdsampleapp.util.ConnectionUtil
import javax.inject.Inject

class GroupChannelPresenterImpl @Inject constructor(private val preferenceHelper: AppPreferenceHelper) :
    GroupChannelPresenter {

    private val CHANNEL_LIST_LIMIT = 15


    private lateinit var view: GroupChannelView
    private var channelCollection: ChannelCollection? = null
    private lateinit var context: Context

    override fun setView(groupView: GroupChannelView) {
        this.view = groupView
    }

    override fun createGroupPressed() {
        view.createGroupPressed()
    }

    override fun backPressed() {
        view.backPressed()
    }

    override fun refresh() {
        refreshChannelList(CHANNEL_LIST_LIMIT)
    }

    override fun onResume(context: Context) {
        this.context = context
        val userId = preferenceHelper.getUserId()

        SendBirdSyncManager.setup(context, userId) {

            val s = SendBird.getConnectionState()
            // (context as BaseApp).setSyncManagerSetUp(true)
            (context as Activity).runOnUiThread {
                if (SendBird.getConnectionState() != SendBird.ConnectionState.OPEN) {
                    refresh()
                }
                ConnectionUtil.addConnectionManagementHandler(
                    AppConstants.CONNECTION_HANDLER_ID,
                    userId,
                    object : ConnectionUtil.ConnectionManagementHandler {
                        override fun onConnected(connected: Boolean) {
                            refresh()
                        }
                    })
            }

        }
    }

    override fun onPause() {
        ConnectionUtil.removeConnectionManagementHandler(AppConstants.CONNECTION_HANDLER_ID)

        if (channelCollection != null) {
            channelCollection!!.setCollectionHandler(null)
            channelCollection!!.remove()
        }

    }

    private fun refreshChannelList(numChannels: Int) {

        if (channelCollection != null) {
            channelCollection!!.remove()
        }

        val query = GroupChannel.createMyGroupChannelListQuery()
        query.limit = numChannels

        channelCollection = ChannelCollection(query)
        channelCollection!!.setCollectionHandler(channelCollectionHandler)
        channelCollection!!.fetch() {
            if (it == null) {
                Log.d("TAG", "Working")
            } else {
                val s = it.code
                val sd = it.localizedMessage
            }
        }

    }

    private val channelCollectionHandler =
        ChannelCollectionHandler { channelCollection, channelList, channelEventAction ->
            Log.d("SyncManager", "onChannelEvent: size = " + channelList.size + ", action = " + channelEventAction)

            (context as Activity).runOnUiThread() {
                when (channelEventAction) {
                    ChannelEventAction.INSERT -> {
                        //view.setUserChannels(channelList)
                        view.insertChannels(channelList, channelCollection.query.order)
                    }
                    ChannelEventAction.UPDATE -> {
                        view.updateChannels(channelList)

                    }
                    ChannelEventAction.REMOVE -> {
                        view.removeChannels(channelList)

                    }
                    ChannelEventAction.MOVE -> {
                        view.moveChannels(channelList, channelCollection.query.order)

                    }
                    ChannelEventAction.CLEAR -> {
                        view.clearChannels()

                    }
                    else -> {

                    }
                }
            }
        }

}