package com.sendbirdsampleapp.ui.login.presenter

import android.util.Log
import com.sendbird.android.SendBird
import com.sendbirdsampleapp.ui.base.presenter.BasePresenter
import com.sendbirdsampleapp.ui.login.interactor.LoginMVPInteractor
import com.sendbirdsampleapp.ui.login.view.LoginMVPView
import com.sendbirdsampleapp.util.AppConstants
import javax.inject.Inject


class LoginPresenter<V : LoginMVPView, I : LoginMVPInteractor> @Inject internal constructor(interactor: I) :
    BasePresenter<V, I>(interactor = interactor), LoginMVPPresenter<V, I> {

    override fun onAttach(view: V) {
        super.onAttach(view)
        isUserLoggedIn()
    }

    override fun onLoginClicked(userId: String, nickname: String) {
        when {
            userId.isEmpty() -> getView()?.showValidationMessage(AppConstants.EMPTY_USER_ID)
            nickname.isEmpty() -> getView()?.showValidationMessage(AppConstants.EMPTY_NICKNAME)
            else -> {
                getView()?.showProgress()
                SendBird.connect(userId) { user, e ->
                    if (e != null) {
                        getView()?.showValidationMessage(AppConstants.FAILED_LOGIN)
                        Log.e(AppConstants.FAILED_LOGIN.toString(), "Failed login") //TODO Add better error handling
                    } else {
                        updateUserInSharedPref(userId, nickname)
                        getView()?.navigateToChannels()
                    }
                }
            }
        }

    }

    private fun isUserLoggedIn() = getView()?.let {
        if (checkUserLoggedIn()) {
            it.navigateToChannels()
        }
    }

    private fun checkUserLoggedIn(): Boolean {
        interactor?.let { return it.isUserLoggedIn() }
        return false
    }

    private fun updateUserInSharedPref(userId: String, nickname: String) = interactor?.updateUserInSharedPref(userId, nickname)


}