package com.sendbirdsampleapp.ui.group_channel.create_group

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.GroupChannel
import com.sendbird.android.User
import com.sendbirdsampleapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.channel_create_view.view.*

class GroupChannelCreateAdapter : RecyclerView.Adapter<GroupChannelCreateAdapter.UserHolder>() {

    private var users: MutableList<User>

    init {
        users = ArrayList()
    }

    fun addUsers(users: MutableList<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): UserHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserHolder(layoutInflater.inflate(R.layout.channel_create_view, parent, false))
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindViews(users[position], position)
    }

    class UserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val checkbox = view.checkbox_channel_create_view
        val userImage = view.image_channel_create_view
        val userId = view.text_channel_create_user_id

        fun bindViews(user: User, position: Int) {

            userId.text = user.userId
            val profile = user.profileUrl
            if (profile != "") {
                Picasso.get().load(user.profileUrl).resize(50,50).into(userImage)
            }

        }


    }
}