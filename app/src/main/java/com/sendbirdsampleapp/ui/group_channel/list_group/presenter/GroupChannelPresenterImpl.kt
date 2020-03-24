package com.sendbirdsampleapp.ui.group_channel.list_group.presenter


import android.app.Activity
import android.content.Context
import android.util.Log
import com.sendbird.android.*
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
import java.util.ArrayList
import javax.inject.Inject

class GroupChannelPresenterImpl @Inject constructor(private val preferenceHelper: AppPreferenceHelper) :
    GroupChannelPresenter {

    private val CHANNEL_LIST_LIMIT = 15


    private lateinit var view: GroupChannelView
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


    override fun onResume(context: Context) {
        this.context = context
    }

    override fun setUpRecyclerView() {
        val query = GroupChannel.createMyGroupChannelListQuery()
        query.limit = CHANNEL_LIST_LIMIT
        query.order = GroupChannelListQuery.Order.LATEST_LAST_MESSAGE

        query.next() { channels, e ->

            if (e != null) {
                Log.e("TAG", e.toString())
            }

            view.setUserChannels(channels)
        }
    }

    override fun onPause() {
        ConnectionUtil.removeConnectionManagementHandler(AppConstants.CONNECTION_HANDLER_ID)
    }

    override fun searchMessages(word: String) {

        val query = MessageSearchQuery.Builder().setKeyword(word).build()

        query.next { message, sendBirdException ->
            if (sendBirdException != null) {
                Log.e("ERROR", sendBirdException.message)
            }
            getChannelList(message)
        }
    }

    override fun clearSearch() {
        view.clearChannels()
        setUpRecyclerView()
    }

    private fun getChannelList(results: MutableList<BaseMessage>) {
        val channels: MutableList<GroupChannel>
        channels = ArrayList()

        val channelUrl: HashMap<String,GroupChannel> = HashMap()

        for (result in results) {
            GroupChannel.getChannel(result.channelUrl) { groupChannel, exception ->

                if (exception != null) {
                    Log.e("TAG", exception.toString())
                }
                if (!channelUrl.containsKey(groupChannel.url)) {
                    channelUrl.put(groupChannel.url, groupChannel)
                    channels.add(groupChannel)
                }
            }
        }

        view.displaySearchResults(channels)
    }
}