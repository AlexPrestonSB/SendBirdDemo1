package com.sendbirdsampleapp.ui.group_channel.list_group

import android.content.Context
import android.provider.Settings.Global.getString
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.*
import com.sendbirdsampleapp.BuildConfig
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.util.DateUtils
import kotlinx.android.synthetic.main.channel_chooser_view.view.*
import kotlin.collections.ArrayList

class GroupChannelListAdapter(context: Context) : RecyclerView.Adapter<GroupChannelListAdapter.ChannelHolder>() {

    private var channels: MutableList<GroupChannel>
    private val context: Context


    init {
        channels = ArrayList()
        this.context = context
    }

    fun addChannels(channels: MutableList<GroupChannel>) {
        this.channels = channels
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChannelHolder(layoutInflater.inflate(R.layout.channel_chooser_view, parent, false))
    }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: ChannelHolder, position: Int) {
        holder.bindViews(context, channels[position], position)
    }

    class ChannelHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        val channelName = v.text_channel_name
        val channelDate = v.text_channel_date
        val channelRecentMessage = v.text_channel_recent
        val channelMemberCount = v.text_channel_member_count


        fun bindViews(context: Context, groupChannel: GroupChannel, position: Int) {

            val lastMessage = groupChannel.lastMessage

            if (lastMessage != null) {
                channelDate.text = DateUtils.formatDateTime(lastMessage.createdAt)

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

            channelName.text = groupChannel.members[0].nickname
            channelMemberCount.text = groupChannel.memberCount.toString()

        }

        override fun onClick(v: View?) {


        }

    }
}