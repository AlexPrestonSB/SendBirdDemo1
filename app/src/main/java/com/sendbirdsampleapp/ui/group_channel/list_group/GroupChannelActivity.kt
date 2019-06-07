package com.sendbirdsampleapp.ui.group_channel.list_group

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.group_channel.list_group.presenter.GroupChannelPresenterImpl
import com.sendbirdsampleapp.ui.group_channel.list_group.view.GroupChannelView
import com.sendbirdsampleapp.util.AppConstants

class GroupChannelActivity : AppCompatActivity(), GroupChannelView {

    private val TAG = "GROUP_CHANNEL_ACTIVITY"

    lateinit var presenter: GroupChannelPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_channel)

        if (savedInstanceState == null) {

        }

        presenter = GroupChannelPresenterImpl()

        presenter.setView(this)
    }

    override fun showValidationMessage(errorCode: Int) {

        when(errorCode) {
            AppConstants.FAILED_GROUP_CREATE -> {
                Toast.makeText(this, getString(R.string.group_channel_create_failed), Toast.LENGTH_LONG).show()
                Log.e(TAG, getString(R.string.group_channel_create_failed))
            }
        }
    }
}