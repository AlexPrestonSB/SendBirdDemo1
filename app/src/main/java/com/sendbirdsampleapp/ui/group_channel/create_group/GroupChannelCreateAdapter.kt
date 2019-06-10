package com.sendbirdsampleapp.ui.group_channel.create_group

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.android.User
import com.sendbirdsampleapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.channel_create_view.view.*



class GroupChannelCreateAdapter(context: Context, listener: OnItemCheckedChangeListener) : RecyclerView.Adapter<GroupChannelCreateAdapter.UserHolder>() {

    interface OnItemCheckedChangeListener {
        fun onItemChecked(user: User, checked: Boolean)
    }


    private var users: MutableList<User>
    private val context: Context
    private lateinit var checkedListener: OnItemCheckedChangeListener

    companion object {
        fun selectedUsers() = ArrayList<String>()
        fun sparseArray() = SparseBooleanArray()
    }

    init {
        users = ArrayList()
        this.context = context
        this.checkedListener = listener
    }

//    fun setItemCheckedChangeListener(listener: OnItemCheckedChangeListener) = checkedListener

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
        holder.bindViews(users[position], position, checkedListener)
    }

    class UserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val checkbox = view.checkbox_channel_create_view
        val userImage = view.image_channel_create_view
        val userId = view.text_channel_create_user_id

        fun bindViews(user: User, position: Int, listener: OnItemCheckedChangeListener) {

            userId.text = user.userId
            val profile = user.profileUrl
            if (profile != "") {
                Picasso.get().load(user.profileUrl).resize(100,100).into(userImage)
            }

            checkbox.isChecked = sparseArray().get(position, false)

            checkbox.setOnCheckedChangeListener() {buttonView, isChecked ->
                listener.onItemChecked(user, isChecked)

                if (isChecked) {
                    selectedUsers().add(user.userId)
                    sparseArray().put(position, true)
                } else {
                    selectedUsers().remove(user.userId)
                    sparseArray().put(position, false)
                }
            }

        }


    }
}