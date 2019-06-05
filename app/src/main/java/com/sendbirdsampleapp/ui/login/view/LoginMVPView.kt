package com.sendbirdsampleapp.ui.login.view

import com.sendbirdsampleapp.ui.base.view.MVPView

interface LoginMVPView : MVPView  {

    fun showValidationMessage(errorCode: Int)
    fun navigateToChannels()

}