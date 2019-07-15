package com.sendbirdsampleapp.ui.group_channel.list_group

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.*
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.util.DateUtil
import com.sendbirdsampleapp.util.SyncManagerUtil
import kotlinx.android.synthetic.main.item_channel_chooser.view.*
import kotlin.collections.ArrayList

class GroupChannelListAdapter(context: Context, listener: OnChannelClickedListener) : RecyclerView.Adapter<GroupChannelListAdapter.ChannelHolder>() {

    interface OnChannelClickedListener {
        fun onItemClicked(channel: GroupChannel)
    }

    private var channels: MutableList<GroupChannel>
    private val context: Context
    private val listener: OnChannelClickedListener



    init {
        channels = ArrayList()
        this.context = context
        this.listener = listener
    }

    fun addChannels(channels: MutableList<GroupChannel>) {
        this.channels = channels
        notifyDataSetChanged()
    }

    fun insertChannels(channelList: List<GroupChannel>, order: GroupChannelListQuery.Order) {
        for (newChannel in channelList) {
            val index = SyncManagerUtil.findIndexOfChannel(channels, newChannel, order)
            channels.add(index, newChannel)
        }

        notifyDataSetChanged()
    }

    fun updateChannels(channelList: List<GroupChannel>) {
        for (updatedChannel in channelList) {
            val index = SyncManagerUtil.getIndexOfChannel(channels, updatedChannel)
            if (index != -1) {
                channels.set(index, updatedChannel)
                notifyItemChanged(index)
            }
        }
    }

    fun moveChannels(channelList: List<GroupChannel>, order: GroupChannelListQuery.Order) {
        for (movedChannel in channelList) {
            val fromIndex = SyncManagerUtil.getIndexOfChannel(channels, movedChannel)
            val toIndex = SyncManagerUtil.findIndexOfChannel(channels, movedChannel, order)
            if (fromIndex != -1) {
                channels.removeAt(fromIndex)
                channels.add(toIndex, movedChannel)
                notifyItemMoved(fromIndex, toIndex)
                notifyItemChanged(toIndex)
            }
        }
    }

    fun removeChannels(channelList: List<GroupChannel>) {
        for (removedChannel in channelList) {
            val index = SyncManagerUtil.getIndexOfChannel(channels, removedChannel)
            if (index != -1) {
                channels.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }

    fun clearChannels() {
        channels.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChannelHolder(layoutInflater.inflate(R.layout.item_channel_chooser, parent, false))
    }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: ChannelHolder, position: Int) {
        holder.bindViews(context, channels[position], position, listener)
    }

    class ChannelHolder(v: View) : RecyclerView.ViewHolder(v) {

        val channelName = v.text_channel_name
        val channelDate = v.text_channel_date
        val channelRecentMessage = v.text_channel_recent
        val channelMemberCount = v.text_channel_member_count


        fun bindViews(context: Context, groupChannel: GroupChannel, position: Int, listener: OnChannelClickedListener) {

            val lastMessage = groupChannel.lastMessage

            if (lastMessage != null) {
                channelDate.text = DateUtil.formatDateTime(lastMessage.createdAt)

                if (lastMessage is UserMessage) {
                    channelRecentMessage.text = lastMessage.message

                } else if (lastMessage is AdminMessage) {
                    channelRecentMessage.text = lastMessage.message

                } else {
                    val fileMessage = lastMessage as FileMessage
                    val sender = String.format(context.getString(R.string.group_channel_list_file_message_text),
                        fileMessage.sender.nickname
                    )
                    channelRecentMessage.text = sender

                }
            }

            itemView.setOnClickListener {
                listener.onItemClicked(groupChannel)
            }

            channelName.text = groupChannel.members[0].nickname
            channelMemberCount.text = groupChannel.memberCount.toString()

        }


    }
}