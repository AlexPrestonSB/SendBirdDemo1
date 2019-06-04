package com.sendbirdsampleapp.ui.base.interactor

interface MVPInteractor {

    fun isUserLoggedIn(): Boolean

    fun preformUserLogout()
}