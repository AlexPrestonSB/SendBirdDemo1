package com.sendbirdsampleapp.ui.login.interactor


import android.content.Context
import com.sendbirdsampleapp.data.preferences.PreferenceHelper
import com.sendbirdsampleapp.ui.base.interactor.BaseInteractor
import javax.inject.Inject


class LoginInteractor @Inject internal constructor(preferenceHelper: PreferenceHelper) : BaseInteractor(preferenceHelper), LoginMVPInteractor  {

    override fun updateUserInSharedPref(userId: String, nickname: String) {
        preferenceHelper.let {
            it.setUserId(userId)
            it.setNickname(nickname)
            it.setConnected(true)
        }
    }

}