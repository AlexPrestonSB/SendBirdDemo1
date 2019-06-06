package com.sendbirdsampleapp.ui.base.presenter

import com.sendbirdsampleapp.ui.base.interactor.MVPInteractor
import com.sendbirdsampleapp.ui.base.view.MVPView

abstract class BasePresenter<V : MVPView, I : MVPInteractor> internal constructor(protected var interactor: I?) : MVPPresenter<V, I> {

    private var view: V? = null
    private val isViewAttached: Boolean get() = view != null

    override fun onAttach(view: V) {
        this.view = view
    }

    override fun getView(): V? = view

    override fun onDetach() {
        view = null
        interactor = null
    }

}