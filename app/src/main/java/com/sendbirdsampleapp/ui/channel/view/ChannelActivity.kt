package com.sendbirdsampleapp.ui.channel.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sendbird.android.BaseChannel
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.BuildConfig
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.channel.interactor.ChannelInteractor
import com.sendbirdsampleapp.ui.channel.presenter.ChannelPresenter
import com.sendbirdsampleapp.ui.group_channel.view.GroupChannelActivity
import com.sendbirdsampleapp.ui.open_channel.view.OpenChannelActivity
import kotlinx.android.synthetic.main.activity_channel_chooser.*

class ChannelActivity : AppCompatActivity(), ChannelView {

    private val presenter =
        ChannelPresenter(this, ChannelInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_chooser)


        textview_channel_group.setOnClickListener { groupChannelPressed() }
        textview_channel_open.setOnClickListener { openChannelPressed() }

        val version = String.format(
            resources.getString(R.string.sample_version),
            BuildConfig.VERSION_NAME,
            SendBird.getSDKVersion().toString()
        )

        textview_channel_version.text = version
    }

    fun groupChannelPressed() {
        presenter.onChannelPressed(BaseChannel.ChannelType.GROUP)
    }

    fun openChannelPressed() {
        presenter.onChannelPressed(BaseChannel.ChannelType.OPEN)
    }

    override fun navigateToGroupChannels() {
        startActivity(Intent(this, GroupChannelActivity::class.java))
    }

    override fun navigateToOpenChannels() {
        startActivity(Intent(this, OpenChannelActivity::class.java))
    }

}