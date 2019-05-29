package com.example.sendbirdsampleapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.sendbirdsampleapp.R
import com.example.sendbirdsampleapp.ui.channel.ChannelActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity: AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter(this, LoginInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        button_login_connect.setOnClickListener { loginPressed()}
    }

    override fun onStart() {
        super.onStart()
//        val prefs = PreferenceUtils.customPrefs(this)
//        if (prefs[UtilConstants.PREFERENCE_KEY_CONNECTED]!!) {
//
//            //TODO Call to connect to SendBird
//      }
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