package com.sendbirdsampleapp.ui.login.view

interface LoginView {

    fun displaySnackBar()

    fun displayError(error: String?)

    fun displayLoading()

    fun dismissLoading()

    fun navigateToChannels()

}