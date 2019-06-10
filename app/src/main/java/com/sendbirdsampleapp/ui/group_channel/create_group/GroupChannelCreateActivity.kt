package com.sendbirdsampleapp.ui.group_channel.create_group

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.R
import kotlinx.android.synthetic.main.activity_channel_create.*
import kotlinx.android.synthetic.main.activity_group_channel.*

class GroupChannelCreateActivity : AppCompatActivity() {

    private val TAG = "GROUP_CREATE_ACTIVITY"

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: GroupChannelCreateAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_create)

        adapter = GroupChannelCreateAdapter()
        recyclerView = recycler_group_create
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userListQuery = SendBird.createApplicationUserListQuery()

        userListQuery.setLimit(100) //TODO change don't user constants
        userListQuery.next() {list, e ->
            if (e != null){
                Log.e(TAG, "Failed to load users")
            } else {
                adapter.addUsers(list)

            }

        }

        loadUsers()
    }

    private fun loadUsers() {

//        val userListQuery = SendBird.createApplicationUserListQuery()
//
//        userListQuery.setLimit(100) //TODO change don't user constants
//        userListQuery.next() {list, e ->
//            if (e != null){
//                Log.e(TAG, "Failed to load users")
//            } else {
//                adapter.addUsers(list)
//
//            }
//
//        }


    }

}