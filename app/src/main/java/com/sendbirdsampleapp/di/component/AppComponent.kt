package com.sendbirdsampleapp.di.component


import android.app.Application
import com.sendbirdsampleapp.BaseApp
import com.sendbirdsampleapp.di.module.AppModule
import com.sendbirdsampleapp.di.module.PresenterModule
import com.sendbirdsampleapp.ui.channel.ChannelActivity
import com.sendbirdsampleapp.ui.login.LoginActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidInjectionModule::class),(PresenterModule::class), (AppModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(target: BaseApp)

    fun inject(loginActivity: LoginActivity)

    //fun inject(channelActivity: ChannelActivity)

}