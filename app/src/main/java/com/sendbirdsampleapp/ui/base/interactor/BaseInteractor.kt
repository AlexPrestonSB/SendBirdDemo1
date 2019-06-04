package com.sendbirdsampleapp.ui.base.interactor

import com.sendbirdsampleapp.data.preferences.PreferenceHelper

open class BaseInteractor() : MVPInteractor {

    protected lateinit var preferenceHelper: PreferenceHelper

    constructor(preferenceHelper: PreferenceHelper) : this() {
        this.preferenceHelper = preferenceHelper
    }


    override fun isUserLoggedIn(): Boolean {
        return this.preferenceHelper.getConnected()
    }

    override fun preformUserLogout() = preferenceHelper.let {
        it.setConnected(false)
        it.setNickname(null)
        it.setUserId(null)
    }
}