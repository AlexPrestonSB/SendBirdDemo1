package com.sendbirdsampleapp.ui.group_channel.create_group

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sendbird.android.User
import com.sendbirdsampleapp.R
import kotlinx.android.synthetic.main.item_gcreate.view.*



class GroupChannelCreateAdapter(context: Context, listener: OnItemCheckedChangeListener) : RecyclerView.Adapter<GroupChannelCreateAdapter.UserHolder>() {

    interface OnItemCheckedChangeListener {
        fun onItemChecked(user: User, checked: Boolean)
    }

    private var users: MutableList<User>
    private val context: Context
    private var checkedListener: OnItemCheckedChangeListener

    companion object {
        fun selectedUsers() = ArrayList<String>()
        fun sparseArray() = SparseBooleanArray()
    }

    init {
        users = ArrayList()
        this.context = context
        this.checkedListener = listener
    }


    fun addUsers(users: MutableList<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): UserHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserHolder(layoutInflater.inflate(R.layout.item_gcreate, parent, false))
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindViews(context, users[position], position, checkedListener)
    }

    class UserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val checkbox = view.checkbox_gcreate
        val userImage = view.image_gcreate
        val userId = view.text_gcreate_user

        fun bindViews(context: Context, user: User, position: Int, listener: OnItemCheckedChangeListener) {

            userId.text = user.userId
            val profile = user.profileUrl
            if (profile != "") {
                Glide.with(context).load(user.profileUrl).apply(RequestOptions().override(100,100)).into(userImage)
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