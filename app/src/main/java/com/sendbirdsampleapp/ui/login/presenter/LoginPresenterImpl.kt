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
            preferenceHelper.getConnected() -> loginView.navigateToChannels()
            userId.isEmpty() -> loginView.showValidationMessage(AppConstants.EMPTY_USER_ID)
            nickname.isEmpty() -> loginView.showValidationMessage(AppConstants.EMPTY_NICKNAME)
            else -> {
                loginView.showProgress()
                SendBird.connect(userId) { user, e ->
                    if (e != null){
                        loginView.showValidationMessage(AppConstants.FAILED_LOGIN)
                        Log.e(AppConstants.FAILED_LOGIN.toString(), "Failed Login")
                    } else {
                        updateUserInSharedPrefs(userId, nickname)
                        loginView.navigateToChannels()
                    }
                }

            }

        }

    }

    private fun updateUserInSharedPrefs(userId: String, nickname: String) =
        preferenceHelper.let {
            it.setConnected(true)
            it.setUserId(userId)
            it.setNickname(nickname)
        }

}