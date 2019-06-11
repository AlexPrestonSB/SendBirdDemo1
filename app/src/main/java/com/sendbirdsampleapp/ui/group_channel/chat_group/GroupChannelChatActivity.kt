package com.sendbirdsampleapp.ui.group_channel.chat_group

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.sendbird.android.BaseMessage
import com.sendbird.android.UserMessage
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.group_channel.chat_group.presenter.GroupChannelChatPresenterImpl
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.ui.group_channel.create_group.GroupChannelCreateAdapter
import com.sendbirdsampleapp.ui.group_channel.list_group.GroupChannelActivity
import kotlinx.android.synthetic.main.activity_group_channel.*
import kotlinx.android.synthetic.main.activity_group_chat.*
import kotlinx.android.synthetic.main.activity_group_create.*

class GroupChannelChatActivity : AppCompatActivity(), GroupChannelChatView {

    private val EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL"


    private lateinit var presenter: GroupChannelChatPresenterImpl

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: GroupChannelChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        presenter = GroupChannelChatPresenterImpl()
        presenter.setView(this)

        presenter.enterChannel(getChannelURl())

        adapter = GroupChannelChatAdapter()
        recyclerView = recycler_group_chat
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

        button_group_chat_back.setOnClickListener { presenter.backPressed() }

        button_group_chat_send.setOnClickListener { presenter.sendMessage(edit_group_chat_message.text.toString()) }
    }

    fun getChannelURl(): String {
        val intent = this.intent
        return intent.getStringExtra(EXTRA_CHANNEL_URL)
    }

    override fun sendMessage(message: UserMessage) {
        adapter.addFirst(message)
        edit_group_chat_message.setText("")
    }

    override fun loadPreviousMessages(messages: MutableList<BaseMessage>) {
        adapter.loadMessages(messages)
    }

    override fun showValidationMessage(errorCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun backPressed() {
        val intent = Intent(this, GroupChannelActivity::class.java)
        startActivity(intent)
    }
}