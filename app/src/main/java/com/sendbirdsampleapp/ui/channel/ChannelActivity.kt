package com.sendbirdsampleapp.ui.channel

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.BuildConfig
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.channel.presenter.ChannelPresenterImpl
import com.sendbirdsampleapp.ui.channel.view.ChannelView
import com.sendbirdsampleapp.ui.group_channel.list_group.GroupChannelActivity
import com.sendbirdsampleapp.ui.open_channel.view.OpenChannelActivity
import kotlinx.android.synthetic.main.activity_channel_chooser.*

class ChannelActivity : AppCompatActivity(), ChannelView {


    lateinit var presenter: ChannelPresenterImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_chooser)

        presenter = ChannelPresenterImpl()
        presenter.setView(this)


        textview_channel_group.setOnClickListener { groupChannelPressed() }
        textview_channel_open.setOnClickListener { openChannelPressed() }

        val version = String.format(
            resources.getString(R.string.sample_version),
            BuildConfig.VERSION_NAME,
            SendBird.getSDKVersion().toString()
        )

        textview_channel_version.text = version
    }

    private fun groupChannelPressed() {
        presenter.navigateToGroupChannels()
    }

    private fun openChannelPressed() {
        presenter.navigateToOpenChannels()
    }

    override fun navigateToGroupChannels() {
        startActivity(Intent(this, GroupChannelActivity::class.java))
    }

    override fun navigateToOpenChannels() {
        startActivity(Intent(this, OpenChannelActivity::class.java))
    }

    override fun showValidationMessage(errorCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}