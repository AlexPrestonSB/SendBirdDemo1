package com.sendbirdsampleapp.ui.login.interactor

import com.sendbirdsampleapp.ui.base.interactor.MVPInteractor

interface LoginMVPInteractor : MVPInteractor {

    fun updateUserInSharedPref(userId: String, nickname: String)


}