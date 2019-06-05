package com.sendbirdsampleapp.di.builder

import com.sendbirdsampleapp.ui.login.LoginActivityModule
import com.sendbirdsampleapp.ui.login.view.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(LoginActivityModule::class)])
    abstract fun bindLoginActivity(): LoginActivity
}