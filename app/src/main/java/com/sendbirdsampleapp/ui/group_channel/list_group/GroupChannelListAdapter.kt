package com.sendbirdsampleapp.ui.group_channel.list_group

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.GroupChannel
import com.sendbirdsampleapp.R

class GroupChannelListAdapter : RecyclerView.Adapter<GroupChannelListAdapter.ChannelHolder>() {

    private val channels: MutableList<GroupChannel>

    init {
        channels = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChannelHolder(layoutInflater.inflate(R.layout.channel_view, parent, false))
    }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: ChannelHolder, position: Int) {
        holder.bindViews(position)
    }

    class ChannelHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val


        fun bindViews(position: Int) {


        }

        override fun onClick(v: View?) {

        }
    }
}