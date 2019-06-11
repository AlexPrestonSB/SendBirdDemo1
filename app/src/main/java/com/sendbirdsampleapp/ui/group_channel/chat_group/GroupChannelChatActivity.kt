package com.sendbirdsampleapp.ui.group_channel.chat_group

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sendbird.android.BaseMessage
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.group_channel.chat_group.presenter.GroupChannelChatPresenterImpl
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.ui.group_channel.list_group.GroupChannelActivity

class GroupChannelChatActivity : AppCompatActivity(), GroupChannelChatView {

    private val EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL"


    private lateinit var presenter: GroupChannelChatPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        presenter = GroupChannelChatPresenterImpl()
        presenter.setView(this)

        presenter.enterChannel(getChannelURl())


    }

    fun getChannelURl(): String {
        val intent = this.intent
        return intent.getStringExtra(EXTRA_CHANNEL_URL)
    }

    override fun loadPreviousMessages(messages: MutableList<BaseMessage>) {
        val messages = messages
    }

    override fun showValidationMessage(errorCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun backPressed() {
        val intent = Intent(this, GroupChannelActivity::class.java)
        startActivity(intent)
    }
}