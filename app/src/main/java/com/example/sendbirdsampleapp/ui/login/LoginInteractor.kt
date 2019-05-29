package com.example.sendbirdsampleapp.ui.login

import com.sendbird.android.SendBird


class LoginInteractor   {

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

                listener.onSuccess()

            }
        }

    }


}