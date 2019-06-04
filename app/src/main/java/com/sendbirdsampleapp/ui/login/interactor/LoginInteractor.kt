package com.sendbirdsampleapp.ui.login.interactor

import com.sendbird.android.SendBird
import com.sendbirdsampleapp.data.preferences.PreferenceHelper
import com.sendbirdsampleapp.ui.base.interactor.BaseInteractor
import javax.inject.Inject


class LoginInteractor @Inject internal constructor(preferenceHelper: PreferenceHelper) : BaseInteractor(preferenceHelper)  {

    interface OnLoginFinishedListener {
        fun onUserIdError()
        fun onNicknameError()
        fun onSuccess()
    }

    fun onLoginClicked(userId: String, nickname: String, listener: OnLoginFinishedListener) {
        when {
            userId.isEmpty() -> listener.onUserIdError()
            nickname.isEmpty() -> listener.onNicknameError()
            else -> SendBird.connect(userId) { user, sendBirdException ->
                if (sendBirdException != null) {
                    listener.onUserIdError()
                }

                preferenceHelper.let {
                    it.setConnected(true)
                    it.setNickname(nickname)
                    it.setUserId(userId)
                }

                listener.onSuccess()

            }
        }

    }


}