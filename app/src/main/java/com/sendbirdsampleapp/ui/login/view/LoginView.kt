package com.sendbirdsampleapp.ui.login.view

interface LoginView {

    fun showProgress()

    fun hideProgress()

    fun navigateToChannels()

    fun showValidationMessage(errorCode: Int)

}