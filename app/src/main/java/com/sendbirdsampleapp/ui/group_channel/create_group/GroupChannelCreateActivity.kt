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
import com.sendbirdsampleapp.ui.group_channel.message_group.GroupChannelMessageActivity
import kotlinx.android.synthetic.main.activity_group_create.*

class GroupChannelCreateActivity : AppCompatActivity(), GroupChannelCreateAdapter.OnItemCheckedChangeListener,
    GroupChannelCreateView {

    private val TAG = "GROUP_CREATE_ACTIVITY"
    private val EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";


    lateinit var recyclerView: RecyclerView

    lateinit var adapter: GroupChannelCreateAdapter

    private lateinit var selectedUsers: ArrayList<String>
    private lateinit var presenter: GroupChannelCreatePresenterImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_create)

        presenter = GroupChannelCreatePresenterImpl()
        presenter.setView(this)

        selectedUsers = ArrayList()

        adapter = GroupChannelCreateAdapter(this, this)
        recyclerView = recycler_group_create
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        text_channel_create.setOnClickListener { presenter.createChannel(selectedUsers) }

        loadUsers()
    }


    override fun createChannelPressed(channelId: String) {
        val intent = Intent(this, GroupChannelMessageActivity::class.java)
        intent.putExtra(EXTRA_NEW_CHANNEL_URL, channelId)
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