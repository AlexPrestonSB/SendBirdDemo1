package com.sendbirdsampleapp.ui.login.presenter

import com.sendbirdsampleapp.ui.base.presenter.MVPPresenter
import com.sendbirdsampleapp.ui.login.interactor.LoginMVPInteractor
import com.sendbirdsampleapp.ui.login.view.LoginMVPView

interface LoginMVPPresenter<V : LoginMVPView, I : LoginMVPInteractor> : MVPPresenter<V, I> {

    fun onLoginClicked(userId: String, nickname: String)



}