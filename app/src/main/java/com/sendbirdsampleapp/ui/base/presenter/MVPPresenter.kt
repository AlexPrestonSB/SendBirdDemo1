package com.sendbirdsampleapp.ui.base.presenter

import com.sendbirdsampleapp.ui.base.interactor.MVPInteractor
import com.sendbirdsampleapp.ui.base.view.MVPView

interface MVPPresenter<V: MVPView, I : MVPInteractor> {

    fun onAttach(view: V)

    fun onDetach()

    fun getView(): V?
}