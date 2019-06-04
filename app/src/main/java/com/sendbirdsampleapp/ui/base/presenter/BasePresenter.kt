package com.sendbirdsampleapp.ui.base.presenter

import com.sendbirdsampleapp.ui.base.interactor.MVPInteractor
import com.sendbirdsampleapp.ui.base.view.MVPView

abstract class BasePresenter<V : MVPView, I : MVPInteractor>