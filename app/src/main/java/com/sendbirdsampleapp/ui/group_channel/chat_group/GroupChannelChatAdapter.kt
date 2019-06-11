package com.sendbirdsampleapp.ui.group_channel.chat_group

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.*
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.util.AppConstants
import com.sendbirdsampleapp.util.DateUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.channel_chat_me_view.view.*
import kotlinx.android.synthetic.main.channel_chat_other_view.view.*

class GroupChannelChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private lateinit var messages: MutableList<BaseMessage>

    init {
        messages = ArrayList()
    }

    fun loadMessages(messages: MutableList<BaseMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            AppConstants.VIEW_TYPE_USER_MESSAGE_ME -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return MyUserHolder(layoutInflater.inflate(R.layout.channel_chat_me_view, parent, false))
            }
            AppConstants.VIEW_TYPE_USER_MESSAGE_OTHER -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return OtherUserHolder(layoutInflater.inflate(R.layout.channel_chat_other_view, parent, false))
            }
            AppConstants.VIEW_TYPE_ADMIN_MESSAGE -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return AdminUserHolder(layoutInflater.inflate(R.layout.channel_chat_admin, parent, false))
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return AdminUserHolder(layoutInflater.inflate(R.layout.channel_chat_admin, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages.get(position)

        when (message) {
            is UserMessage -> {
                if (message.sender.userId.equals(SendBird.getCurrentUser().userId)) {
                    return AppConstants.VIEW_TYPE_USER_MESSAGE_ME
                } else {
                    return AppConstants.VIEW_TYPE_USER_MESSAGE_OTHER
                }
            }
            is FileMessage -> {
                return -1; //TODO
            }
            is AdminMessage -> {
                return AppConstants.VIEW_TYPE_ADMIN_MESSAGE
            }
            else -> return -1
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages.get(position)
        var isNewDay = false

        when {
            (position < messages.size - 1) -> {
                val previousMessage = messages.get(position + 1)

                isNewDay = !DateUtils.isSameDay(message.createdAt, previousMessage.createdAt)
            }
            (position == messages.size - 1) -> {
                isNewDay = true
            }
        }

        when (holder.itemViewType) {
            AppConstants.VIEW_TYPE_USER_MESSAGE_ME -> {
                holder as MyUserHolder
                holder.bindView(messages.get(position) as UserMessage, position, isNewDay)
            }
            AppConstants.VIEW_TYPE_USER_MESSAGE_OTHER -> {
                holder as OtherUserHolder
                holder.bindView(messages.get(position) as UserMessage, position, isNewDay)
            }
            AppConstants.VIEW_TYPE_ADMIN_MESSAGE -> {
                holder as AdminUserHolder
                holder.bindView(messages.get(position), position)
            }
        }

    }

    class MyUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageText = view.text_group_chat_me_message
        val date = view.text_group_chat_me_date
        val messageDate = view.text_group_chat_me_message_date
        val read = view.text_group_chat_me_read

        val urlContainer = view.layout_group_chat_me_link
        val urlName = view.text_group_chat_me_site
        val urlTitle = view.text_group_chat_me_title
        val urlDescription = view.text_group_chat_me_description
        val urlImage = view.image_group_chat_me_url

        fun bindView(message: UserMessage, position: Int, isNewDay: Boolean) {

            messageText.text = message.message
            messageDate.text = DateUtils.formatTime(message.createdAt)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtils.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            urlContainer.visibility = View.GONE
            //TODO set the rest
        }
    }

    class OtherUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageText = view.text_group_chat_other_message
        val date = view.text_group_chat_other_date
        val messageDate = view.text_group_chat_other_message_date
        val read = view.text_group_chat_other_read
        val profileImage = view.image_group_chat_other_profile
        val user = view.text_group_chat_other_user

        val urlContainer = view.layout_group_chat_other_link
        val urlName = view.text_group_chat_other_site
        val urlTitle = view.text_group_chat_other_title
        val urlDescription = view.text_group_chat_other_description
        val urlImage = view.image_group_chat_other_url

        fun bindView(message: UserMessage, position: Int, isNewDay: Boolean) {

            messageText.text = message.message
            messageDate.text = DateUtils.formatTime(message.createdAt)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtils.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            Picasso.get().load(message.sender.profileUrl).resize(50,50).into(profileImage)
            user.text = message.sender.nickname


            urlContainer.visibility = View.GONE
            //TODO set the rest

        }
    }

    class AdminUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(message: BaseMessage, position: Int) {

        }

    }
}