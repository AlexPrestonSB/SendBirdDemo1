package com.sendbirdsampleapp.ui.login.presenter

import android.content.Context
import android.util.Log
import com.sendbird.android.SendBird
import com.sendbird.calls.AuthenticateParams
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdException
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.login.view.LoginView
import com.sendbirdsampleapp.util.AppConstants
import com.sendbirdsampleapp.util.PushUtil
//import org.omg.PortableServer.IdAssignmentPolicyValue.USER_ID
//import java.rmi.dgc.VMID.isUnique
import javax.inject.Inject


class LoginPresenterImpl @Inject constructor(private val preferenceHelper: AppPreferenceHelper) :
    LoginPresenter {

    private lateinit var loginView: LoginView
    private lateinit var context: Context
    private lateinit var userId: String
    private lateinit var nickname: String


    override fun setView(loginView: LoginView) {
        this.loginView = loginView
    }

    override fun onResume(context: Context) {
        this.context = context
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
        this.userId = userId
        this.nickname = nickname

        connectToCalls()
    }

    private fun connectToCalls() {
        PushUtil.getPushToken(context, pushTokenHandler)
    }

    private val pushTokenHandler = object : PushUtil.GetPushTokenHandler {
        override fun onResult(token: String?, e: SendBirdException?) {
            SendBirdCall.authenticate(AuthenticateParams(userId).setAccessToken("")) { user, exception ->
                if (exception == null) {
                    SendBirdCall.registerPushToken(token, false) {
                        Log.d("T", token)
                        if (it == null) {
                            updateCurrentUserInfo(userId, nickname, token)
                            loginView.navigateToChannels()
                        }
                    }
                } else {
                    loginView.showValidationMessage(AppConstants.FAILED_LOGIN)
                }
            }
        }
    }

    private fun updateCurrentUserInfo(userId: String, nickname: String, token: String?) {

        SendBird.updateCurrentUserInfo(nickname, null) { e ->
            if (e != null) {
                loginView.showValidationMessage(AppConstants.FAILED_UPDATE_USER)
            }
            updateUserInSharedPrefs(userId, nickname, token)

        }
    }


    private fun updateUserInSharedPrefs(userId: String, nickname: String, token: String?) {
        preferenceHelper.let {
            it.setConnected(true)
            it.setUserId(userId)
            it.setNickname(nickname)
            it.setToken(token)
        }
        PushUtil.registerPushTokenForCurrentUser(preferenceHelper.getToken(), null)
    }

}