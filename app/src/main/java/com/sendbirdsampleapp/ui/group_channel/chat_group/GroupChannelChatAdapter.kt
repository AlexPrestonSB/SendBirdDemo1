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
import kotlinx.android.synthetic.main.item_gchat_me.view.*
import kotlinx.android.synthetic.main.item_gchat_other.view.*

class GroupChannelChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private lateinit var messages: MutableList<BaseMessage>

    init {
        messages = ArrayList()
    }

    fun loadMessages(messages: MutableList<BaseMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    fun addFirst(message: BaseMessage) {
        messages.add(0, message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            AppConstants.VIEW_TYPE_USER_MESSAGE_ME -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return MyUserHolder(layoutInflater.inflate(R.layout.item_gchat_me, parent, false))
            }
            AppConstants.VIEW_TYPE_USER_MESSAGE_OTHER -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return OtherUserHolder(layoutInflater.inflate(R.layout.item_gchat_other, parent, false))
            }
            AppConstants.VIEW_TYPE_ADMIN_MESSAGE -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return AdminUserHolder(layoutInflater.inflate(R.layout.item_gchat_admin, parent, false))
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                return AdminUserHolder(layoutInflater.inflate(R.layout.item_gchat_admin, parent, false))
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

        val messageText = view.text_gchat_message_me
        val date = view.text_gchat_date_me
        val messageDate = view.text_gchat_timestamp_me
        val read = view.text_gchat_read_me

        val urlContainer = view.layout_gchat_link_me
        val urlName = view.text_gchat_site_me
        val urlTitle = view.text_gchat_title_me
        val urlDescription = view.text_gchat_description_me
        val urlImage = view.image_gchat_url_me

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

        val messageText = view.text_gchat_message_other
        val date = view.text_gchat_date_other
        val messageDate = view.text_gchat_timestamp_other
        val read = view.text_gchat_read_other
        val profileImage = view.image_gchat_profile_other
        val user = view.text_gchat_user_other

        val urlContainer = view.layout_gchat_link_other
        val urlName = view.text_gchat_site_other
        val urlTitle = view.text_gchat_title_other
        val urlDescription = view.text_gchat_description_other
        val urlImage = view.image_gchat_url_other

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