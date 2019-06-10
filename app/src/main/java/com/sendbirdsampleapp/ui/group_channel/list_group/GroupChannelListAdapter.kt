package com.sendbirdsampleapp.ui.group_channel.list_group

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.BaseMessage
import com.sendbird.android.GroupChannel
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.util.DateUtils
import kotlinx.android.synthetic.main.channel_view.view.*
import kotlin.collections.ArrayList

class GroupChannelListAdapter : RecyclerView.Adapter<GroupChannelListAdapter.ChannelHolder>() {

    private var channels: MutableList<GroupChannel>

    init {
        channels = ArrayList()
    }

    fun addChannels(channels: MutableList<GroupChannel>) {
        this.channels = channels
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChannelHolder(layoutInflater.inflate(R.layout.channel_view, parent, false))
    }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: ChannelHolder, position: Int) {
        holder.bindViews(channels[position], position)
    }

    class ChannelHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        val channelName = v.text_channel_name
        val channelDate = v.text_channel_date
        val channelRecentMessage = v.text_channel_recent
        val channelMemberCount= v.text_channel_member_count


        fun bindViews(groupChannel: GroupChannel, position: Int) {

            val lastMessage = groupChannel.lastMessage

            channelName.text = groupChannel.members[0].nickname
            channelDate.text = DateUtils.formatDateTime(groupChannel.lastMessage.createdAt)
            channelRecentMessage.text = lastMessage.messageId.toString()
            channelMemberCount.text = groupChannel.memberCount.toString()

        }

        override fun onClick(v: View?) {


        }

    }
}