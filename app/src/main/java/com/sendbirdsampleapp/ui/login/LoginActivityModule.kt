package com.sendbirdsampleapp.ui.login

import com.sendbirdsampleapp.ui.login.interactor.LoginInteractor
import com.sendbirdsampleapp.ui.login.interactor.LoginMVPInteractor
import com.sendbirdsampleapp.ui.login.presenter.LoginMVPPresenter
import com.sendbirdsampleapp.ui.login.presenter.LoginPresenter
import com.sendbirdsampleapp.ui.login.view.LoginMVPView
import dagger.Module
import dagger.Provides

@Module
class LoginActivityModule {

    @Provides
    internal fun provideLoginInteractor(interactor: LoginInteractor): LoginMVPInteractor = interactor

    @Provides
    internal fun provideLoginPresenter(presenter: LoginPresenter<LoginMVPView, LoginMVPInteractor>): LoginMVPPresenter<LoginMVPView, LoginMVPInteractor> = presenter
}