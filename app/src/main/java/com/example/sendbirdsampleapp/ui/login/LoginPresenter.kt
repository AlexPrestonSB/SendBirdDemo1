package com.example.sendbirdsampleapp.ui.login


class LoginPresenter(var loginView: LoginView?, val loginInteractor: LoginInteractor): LoginInteractor.OnLoginFinishedListener {

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