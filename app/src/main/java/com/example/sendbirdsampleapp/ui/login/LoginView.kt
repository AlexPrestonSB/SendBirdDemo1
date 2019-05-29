package com.example.sendbirdsampleapp.ui.login

interface LoginView {

    fun displaySnackBar()

    fun displayError(error: String?)

    fun displayLoading()

    fun dismissLoading()

    fun navigateToChannels()

}