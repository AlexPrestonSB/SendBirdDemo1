package com.sendbirdsampleapp.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.sendbirdsampleapp.BuildConfig
import com.sendbirdsampleapp.R
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.ui.channel.view.ChannelActivity
import com.sendbirdsampleapp.ui.login.interactor.LoginInteractor
import com.sendbirdsampleapp.ui.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginView {

    private val presenter =
        LoginPresenter(this, LoginInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        button_login_connect.setOnClickListener { loginPressed() }


        val version = String.format(
            resources.getString(R.string.sample_version),
            BuildConfig.VERSION_NAME,
            SendBird.getSDKVersion().toString()
        )

        textview_login_version.text = version
    }

    override fun onStart() {
        super.onStart()

    }

    private fun loginPressed() {
        presenter.onLoginPressed(edittext_login_user_id.text.toString(), edittext_login_nickname.text.toString())
    }

    //Impl of LoginView
    override fun displaySnackBar() {
        //Snackbar snackbar = Snackbar.make(m)

    }

    override fun displayError(error: String?) {
        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading() {
        progress_bar_login.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar_login.visibility = View.GONE
    }

    override fun navigateToChannels() {
        startActivity(Intent(this, ChannelActivity::class.java))
    }
}