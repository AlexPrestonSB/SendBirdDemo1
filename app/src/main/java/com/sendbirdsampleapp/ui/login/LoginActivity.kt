package com.sendbirdsampleapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.BaseApp.Companion.app
import com.sendbirdsampleapp.BuildConfig
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.channel.view.ChannelActivity
import com.sendbirdsampleapp.ui.login.presenter.LoginPresenter
import com.sendbirdsampleapp.ui.login.view.LoginView
import com.sendbirdsampleapp.util.AppConstants
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginView {

    @Inject lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app(this).injector.inject(this)


        button_login_connect.setOnClickListener { loginPressed() }


        presenter.setView(this)

        val version = String.format(
            resources.getString(R.string.sample_version),
            BuildConfig.VERSION_NAME,
            SendBird.getSDKVersion().toString()
        )

        textview_login_version.text = version

    }

    private fun loginPressed() {
        presenter.onLoginClicked(edittext_login_user_id.toString(), edittext_login_nickname.toString())

    }

    override fun showProgress() {
        progress_bar_login.show()
    }

    override fun hideProgress() {
        progress_bar_login.hide()
    }

    override fun navigateToChannels() {
        val intent = Intent(this, ChannelActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showValidationMessage(errorCode: Int) {
        when (errorCode) {
            AppConstants.FAILED_LOGIN -> Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_LONG).show()
            AppConstants.EMPTY_USER_ID -> Toast.makeText(this, getString(R.string.user_id_missing), Toast.LENGTH_LONG).show()
            AppConstants.EMPTY_NICKNAME -> Toast.makeText(this, getString(R.string.nickname_missing), Toast.LENGTH_LONG).show()
        }
    }
}