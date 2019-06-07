package com.sendbirdsampleapp.di.module

import android.app.Application
import android.content.Context
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.data.preferences.PreferenceHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application


    @Provides
    @Singleton
    internal fun providePrefHelper(appPreferenceHelper: AppPreferenceHelper): PreferenceHelper = appPreferenceHelper

}