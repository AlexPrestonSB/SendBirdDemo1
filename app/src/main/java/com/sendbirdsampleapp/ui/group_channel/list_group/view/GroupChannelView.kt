package com.sendbirdsampleapp.ui.group_channel.list_group.view

import com.sendbird.android.GroupChannel

interface GroupChannelView {

    fun showValidationMessage(errorCode: Int)

    fun createGroupPressed()



}