package com.sendbirdsampleapp.ui.base.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity(), MVPView {

    override fun onCreate(savedInstanceState: Bundle?) {
        preformDI()
        super.onCreate(savedInstanceState)
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun preformDI() = AndroidInjection.inject(this)
}