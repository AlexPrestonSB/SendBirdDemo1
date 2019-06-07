package com.sendbirdsampleapp.ui.login.presenter

import com.sendbirdsampleapp.ui.login.view.LoginView

interface LoginPresenter {
    fun setView(loginView: LoginView)

    fun onLoginClicked(userId: String, nickname: String)

}