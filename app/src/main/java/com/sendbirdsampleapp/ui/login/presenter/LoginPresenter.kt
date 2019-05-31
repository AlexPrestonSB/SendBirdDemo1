package com.sendbirdsampleapp.ui.login.presenter

import com.sendbirdsampleapp.ui.login.interactor.LoginInteractor
import com.sendbirdsampleapp.ui.login.view.LoginView


class LoginPresenter(var loginView: LoginView?, val loginInteractor: LoginInteractor):
    LoginInteractor.OnLoginFinishedListener {

    fun onLoginPressed(username: String, nickname: String) {
        loginView?.displayLoading()
        loginInteractor.onLoginClicked(username, nickname, this)
    }

    override fun onUserIdError() {
        loginView?.apply {
            onUserIdError()
            dismissLoading()
        }
    }

    override fun onNicknameError() {
        loginView?.apply {
            onNicknameError()
            dismissLoading()
        }
    }

    override fun onSuccess() {
        loginView?.navigateToChannels()
    }
}