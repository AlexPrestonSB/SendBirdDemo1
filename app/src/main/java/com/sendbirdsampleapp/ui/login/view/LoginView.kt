package com.sendbirdsampleapp.ui.login.view

interface LoginView {

    fun showProgress()

    fun hideProgress()

    fun showValidationMessage(errorCode: Int)

    fun navigateToChannels()

}