package com.sendbirdsampleapp.di.module

import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.login.presenter.LoginPresenter
import com.sendbirdsampleapp.ui.login.presenter.LoginPresenterImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun provideLoginPresenter(prefs: AppPreferenceHelper): LoginPresenter = LoginPresenterImpl(prefs)


}