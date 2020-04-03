package com.sendbirdsampleapp.ui.login.presenter

import android.content.Context
import com.sendbirdsampleapp.ui.login.view.LoginView

interface LoginPresenter {
    fun setView(loginView: LoginView)

    fun onLoginClicked(userId: String, nickname: String)

    fun checkConnected()

    fun onResume(context: Context)

}