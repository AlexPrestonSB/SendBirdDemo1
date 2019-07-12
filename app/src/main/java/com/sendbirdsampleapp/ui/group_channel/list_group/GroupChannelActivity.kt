package com.sendbirdsampleapp.ui.group_channel.list_group

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.sendbird.android.*
import com.sendbirdsampleapp.BaseApp
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.channel.ChannelActivity
import com.sendbirdsampleapp.ui.group_channel.create_group.GroupChannelCreateActivity
import com.sendbirdsampleapp.ui.group_channel.list_group.presenter.GroupChannelPresenterImpl
import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView
import com.sendbirdsampleapp.ui.group_channel.chat_group.GroupChannelChatActivity
import kotlinx.android.synthetic.main.activity_gchannel.*
import javax.inject.Inject

class GroupChannelActivity : AppCompatActivity(), GroupChannelView, GroupChannelListAdapter.OnChannelClickedListener {

    private val TAG = "GROUP_CHANNEL_ACTIVITY"
    private val EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL"


    @Inject
    lateinit var presenter: GroupChannelPresenterImpl

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: GroupChannelListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gchannel)
        BaseApp.app(this).injector.inject(this)


        presenter.setView(this)

        adapter = GroupChannelListAdapter(this, this)
        recyclerView = recycler_group_channels
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //presenter.refresh()

        fab_group_channel_create.setOnClickListener { presenter.createGroupPressed() }

        button_gchannel_back.setOnClickListener { presenter.backPressed()}

    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    override fun backPressed() {
        val intent = Intent(this, ChannelActivity::class.java)
        startActivity(intent)
    }

    override fun showValidationMessage(errorCode: Int) {

    }

    override fun createGroupPressed() {
        val intent = Intent(this, GroupChannelCreateActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClicked(channel: GroupChannel) {
        val intent = Intent(this, GroupChannelChatActivity::class.java)
        intent.putExtra(EXTRA_CHANNEL_URL, channel.url)
        startActivity(intent)

    }

    override fun setUserChannels(channels: MutableList<GroupChannel>) {
        runOnUiThread{
            adapter.addChannels(channels)
        }
    }
//
//    private fun setUserChannels() {
//
//
//
//        val channelListQuery = GroupChannel.createMyGroupChannelListQuery()
//        channelListQuery.isIncludeEmpty = true
//        channelListQuery.limit = 100
//
//        channelListQuery.next() { channels, e ->
//            if (e != null) {
//                Log.e("TAG", e.printStackTrace().toString())
//            } else {
//                adapter.addChannels(channels)
//            }
//        }
//    }
}