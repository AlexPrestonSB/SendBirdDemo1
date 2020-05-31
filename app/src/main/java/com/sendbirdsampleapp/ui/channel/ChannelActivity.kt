package com.sendbirdsampleapp.ui.channel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.BaseApp
import com.sendbirdsampleapp.BuildConfig
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.calls.CallActivity
import com.sendbirdsampleapp.ui.calls.DialActivity
import com.sendbirdsampleapp.ui.channel.presenter.ChannelPresenterImpl
import com.sendbirdsampleapp.ui.channel.view.ChannelView
import com.sendbirdsampleapp.ui.group_channel.list_group.GroupChannelActivity
import com.sendbirdsampleapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_channel_chooser.*
import javax.inject.Inject

class ChannelActivity : AppCompatActivity(), ChannelView {


    @Inject
    lateinit var presenter: ChannelPresenterImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_chooser)

        BaseApp.app(this).injector.inject(this)

        presenter.setView(this)


        //text_channel_group.setOnClickListener { groupChannelPressed() }
        button_calls.setOnClickListener{callsPressed()}


        val version = String.format(
            resources.getString(R.string.sample_version),
            BuildConfig.VERSION_NAME,
            SendBird.getSDKVersion().toString()
        )

        text_channel_version.text = version
        button_channel_logout.setOnClickListener {
           // SendBirdSyncManager.getInstance().clearCache()
            presenter.logoutPressed()
        }

    }

    private fun groupChannelPressed() {
        presenter.navigateToGroupChannels()
    }

    private fun callsPressed() {
        presenter.navigateToCalls()
    }


    override fun logoutPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToGroupChannels() {
        startActivity(Intent(this, GroupChannelActivity::class.java))
    }

    override fun navigateToCalls() {
        startActivity(Intent(this, DialActivity::class.java))
    }

    override fun showValidationMessage(errorCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}