package com.sendbirdsampleapp.ui.login.presenter

import android.util.Log
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.login.view.LoginView
import com.sendbirdsampleapp.util.AppConstants
import javax.inject.Inject

class LoginPresenterImpl @Inject constructor(private val preferenceHelper: AppPreferenceHelper) : LoginPresenter {

    private lateinit var loginView: LoginView

    override fun setView(loginView: LoginView) {
        this.loginView = loginView
    }


    override fun onLoginClicked(userId: String, nickname: String) {

        when {
            userId.isEmpty() -> loginView.showValidationMessage(AppConstants.EMPTY_USER_ID)
            nickname.isEmpty() -> loginView.showValidationMessage(AppConstants.EMPTY_NICKNAME)
            else -> {
                loginView.showProgress()
                connectToSendBird(userId, nickname)
            }

        }

    }

    override fun checkConnected() {
        if (preferenceHelper.getConnected()) {
            loginView.showProgress()
            connectToSendBird(preferenceHelper.getUserId(), preferenceHelper.getNickname())
        }
    }

    private fun connectToSendBird(userId: String, nickname: String) {
        SendBird.connect(userId) { user, e ->
            if (e != null){
                loginView.showValidationMessage(AppConstants.FAILED_LOGIN)
                Log.e(AppConstants.FAILED_LOGIN.toString(), "Failed Login")
            } else {
                updateCurrentUserInfo(userId, nickname)
                loginView.navigateToChannels()
            }
        }
    }

    private fun updateCurrentUserInfo(userId: String, nickname: String) {

        SendBird.updateCurrentUserInfo(nickname, null) { e ->
            if(e != null) {
              Log.e("TEST", "TEST") //TODO
            }
            updateUserInSharedPrefs(userId, nickname)

        }
    }


    private fun updateUserInSharedPrefs(userId: String, nickname: String) =
        preferenceHelper.let {
            it.setConnected(true)
            it.setUserId(userId)
            it.setNickname(nickname)
        }

}