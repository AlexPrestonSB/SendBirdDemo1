package com.sendbirdsampleapp.ui.group_channel.create_group

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.sendbird.android.SendBird
import com.sendbird.android.User
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.group_channel.create_group.presenter.GroupChannelCreatePresenterImpl
import com.sendbirdsampleapp.ui.group_channel.create_group.view.GroupChannelCreateView
import com.sendbirdsampleapp.ui.group_channel.list_group.GroupChannelActivity
import com.sendbirdsampleapp.ui.group_channel.chat_group.GroupChannelChatActivity
import kotlinx.android.synthetic.main.activity_gcreate.*

class GroupChannelCreateActivity : AppCompatActivity(), GroupChannelCreateAdapter.OnItemCheckedChangeListener,
    GroupChannelCreateView {

    private val TAG = "GROUP_CREATE_ACTIVITY"
    private val EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL"


    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: GroupChannelCreateAdapter

    private lateinit var selectedUsers: ArrayList<String>
    private lateinit var presenter: GroupChannelCreatePresenterImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gcreate)

        presenter = GroupChannelCreatePresenterImpl()
        presenter.setView(this)

        selectedUsers = ArrayList()

        adapter = GroupChannelCreateAdapter(this, this)
        recyclerView = recycler_gcreate
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        button_gcreate.setOnClickListener { presenter.createChannel(selectedUsers) }

        button_gcreate_back.setOnClickListener{ presenter.backPressed()}

        loadUsers()
    }

    override fun backPressed() {
        val intent = Intent(this, GroupChannelActivity::class.java)
        startActivity(intent)
    }

    override fun createChannelPressed(channelId: String) {
        val intent = Intent(this, GroupChannelChatActivity::class.java)
        intent.putExtra(EXTRA_CHANNEL_URL, channelId)
        startActivity(intent)

    }

    override fun showValidationMessage(errorCode: Int) {
    }

    override fun onItemChecked(user: User, checked: Boolean) {
        if (checked) {
            selectedUsers.add(user.userId)
        } else {
            selectedUsers.remove(user.userId)
        }

    }

    //TODO rewrite this function it is so bad
    private fun loadUsers() {
        Thread {
            val userListQuery = SendBird.createApplicationUserListQuery()

            userListQuery.setLimit(100) //TODO change don't use constants
            userListQuery.next() { list, e ->
                if (e != null) {
                    Log.e(TAG, "Failed to load users")
                } else {

                    this.runOnUiThread {
                        adapter.addUsers(list)
                    }
                }

            }

        }.start()


    }

}