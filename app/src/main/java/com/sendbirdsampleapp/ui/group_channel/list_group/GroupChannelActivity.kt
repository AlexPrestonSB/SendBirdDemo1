package com.sendbirdsampleapp.ui.group_channel.list_group

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.sendbird.android.*
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.group_channel.create_group.GroupChannelCreateActivity
import com.sendbirdsampleapp.ui.group_channel.list_group.presenter.GroupChannelPresenterImpl
import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView
import com.sendbirdsampleapp.util.AppConstants
import kotlinx.android.synthetic.main.activity_group_channel.*

class GroupChannelActivity : AppCompatActivity(), GroupChannelView {

    private val TAG = "GROUP_CHANNEL_ACTIVITY"

    lateinit var presenter: GroupChannelPresenterImpl

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: GroupChannelListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_channel)


        presenter = GroupChannelPresenterImpl()

        presenter.setView(this)

        adapter = GroupChannelListAdapter()
        recyclerView = recycler_group_channels
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setUserChannels()

        fab_group_channel_create.setOnClickListener { presenter.createGroupPressed() }

    }

    override fun showValidationMessage(errorCode: Int) {

        when (errorCode) {
            AppConstants.FAILED_GROUP_CREATE -> {
                Toast.makeText(this, getString(R.string.group_channel_create_failed), Toast.LENGTH_LONG).show()
                Log.e(TAG, getString(R.string.group_channel_create_failed))
            }
        }
    }

    override fun createGroupPressed() {
        val intent = Intent(this, GroupChannelCreateActivity::class.java)
        startActivity(intent)
    }


    private fun setUserChannels() {

        val channelListQuery = GroupChannel.createMyGroupChannelListQuery()
        channelListQuery.isIncludeEmpty = true
        channelListQuery.limit = 100

        channelListQuery.next() { channels, e ->
            if (e != null) {
                Log.e("TAG", e.printStackTrace().toString())
            } else {
                adapter.addChannels(channels)
            }
        }
    }
}