package com.sendbirdsampleapp.ui.group_channel.chat_group

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sendbird.android.*
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.data.UrlInfo
import com.sendbirdsampleapp.util.*
import kotlinx.android.synthetic.main.item_gchat_admin.view.*
import kotlinx.android.synthetic.main.item_gchat_file_me.view.*
import kotlinx.android.synthetic.main.item_gchat_file_other.view.*
import kotlinx.android.synthetic.main.item_gchat_image_me.view.*
import kotlinx.android.synthetic.main.item_gchat_image_other.view.*
import kotlinx.android.synthetic.main.item_gchat_map_me.view.*
import kotlinx.android.synthetic.main.item_gchat_map_other.view.*
import kotlinx.android.synthetic.main.item_gchat_me.view.*
import kotlinx.android.synthetic.main.item_gchat_me.view.text_gchat_date_me
import kotlinx.android.synthetic.main.item_gchat_me.view.text_gchat_read_me
import kotlinx.android.synthetic.main.item_gchat_me.view.text_gchat_timestamp_me
import kotlinx.android.synthetic.main.item_gchat_other.view.*
import kotlinx.android.synthetic.main.item_gchat_other.view.image_gchat_profile_other
import kotlinx.android.synthetic.main.item_gchat_other.view.text_gchat_date_other
import kotlinx.android.synthetic.main.item_gchat_other.view.text_gchat_read_other
import kotlinx.android.synthetic.main.item_gchat_other.view.text_gchat_timestamp_other
import kotlinx.android.synthetic.main.item_gchat_other.view.text_gchat_user_other
import kotlinx.android.synthetic.main.item_gchat_video_me.view.*
import kotlinx.android.synthetic.main.item_gchat_video_other.view.*
import org.json.JSONException
import kotlin.collections.ArrayList

class GroupChannelChatAdapter(context: Context, listener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onUserMessageClick(message: UserMessage)
        fun onFileMessageClicked(message: FileMessage)
    }


    private var messages: MutableList<BaseMessage>
    private var context: Context
    private var listener: OnItemClickListener

    init {
        messages = ArrayList()
        this.context = context
        this.listener = listener
    }

    fun loadMessages(messages: MutableList<BaseMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    fun addFirst(message: BaseMessage) {
        messages.add(0, message)
        notifyDataSetChanged()
    }

    fun insert(messageList: MutableList<BaseMessage>) {
        for (message in messageList) {
            val index = SyncManagerUtil.findIndexOfMessage(messages, message)
            this.messages.add(index, message)
            notifyItemInserted(index)
        }
    }

    fun update(messageList: MutableList<BaseMessage>) {
        for (message in messageList) {
            val index = SyncManagerUtil.findIndexOfMessage(messages, message)
            if (index != -1) {
                this.messages.add(index, message)
                notifyItemChanged(index)
            }
        }
    }

    fun remove(messageList: MutableList<BaseMessage>) {
        for (message in messageList) {
            val index = SyncManagerUtil.findIndexOfMessage(messages, message)
            if (index != -1) {
                this.messages.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }

    fun clear() {
        messages.clear()
        notifyDataSetChanged()
    }

    fun markAsRead() {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        when (viewType) {
            AppConstants.VIEW_TYPE_USER_MESSAGE_ME -> {
                return MyUserHolder(layoutInflater.inflate(R.layout.item_gchat_me, parent, false))
            }
            AppConstants.VIEW_TYPE_USER_MESSAGE_OTHER -> {
                return OtherUserHolder(layoutInflater.inflate(R.layout.item_gchat_other, parent, false))
            }
            AppConstants.VIEW_TYPE_IMAGE_MESSAGE_ME -> {
                return MyImageHolder(layoutInflater.inflate(R.layout.item_gchat_image_me, parent, false))
            }
            AppConstants.VIEW_TYPE_IMAGE_MESSAGE_OTHER -> {
                return OtherImageHolder(layoutInflater.inflate(R.layout.item_gchat_image_other, parent, false))
            }
            AppConstants.VIEW_TYPE_VIDEO_MESSAGE_ME -> {
                return MyVideoHolder(layoutInflater.inflate(R.layout.item_gchat_video_me, parent, false))
            }
            AppConstants.VIEW_TYPE_VIDEO_MESSAGE_OTHER -> {
                return OtherVideoHolder(layoutInflater.inflate(R.layout.item_gchat_video_other, parent, false))
            }
            AppConstants.VIEW_TYPE_FILE_MESSAGE_ME -> {
                return MyFileMessage(layoutInflater.inflate(R.layout.item_gchat_file_me, parent, false))
            }
            AppConstants.VIEW_TYPE_FILE_MESSAGE_OTHER -> {
                return OtherFileMessage(layoutInflater.inflate(R.layout.item_gchat_file_other, parent, false))
            }
            AppConstants.VIEW_TYPE_ADMIN_MESSAGE -> {
                return AdminUserHolder(layoutInflater.inflate(R.layout.item_gchat_admin, parent, false))
            }
            AppConstants.VIEW_TYPE_LOCATION_ME -> {
                return MyLocationHolder(layoutInflater.inflate(R.layout.item_gchat_map_me, parent, false))
            }
            AppConstants.VIEW_TYPE_LOCATION_OTHER -> {
                return OtherLocationHolder(layoutInflater.inflate(R.layout.item_gchat_map_other, parent, false))
            }
            else ->
                return AdminUserHolder(layoutInflater.inflate(R.layout.item_gchat_admin, parent, false))
        }
    }


    override fun getItemViewType(position: Int): Int {
        val message = messages.get(position)

        when (message) {
            is UserMessage -> {
                return if (message.sender.userId == SendBird.getCurrentUser().userId) {
                    if (message.customType == "location") {
                        AppConstants.VIEW_TYPE_LOCATION_ME
                    } else {
                        AppConstants.VIEW_TYPE_USER_MESSAGE_ME
                    }
                } else {
                    if (message.customType == "location") {
                        AppConstants.VIEW_TYPE_LOCATION_OTHER
                    } else {
                        AppConstants.VIEW_TYPE_USER_MESSAGE_OTHER
                    }
                }
            }
            is FileMessage -> {
                return if (message.type.toLowerCase().startsWith("image")) {
                    if (message.sender.userId.equals(SendBird.getCurrentUser().userId)) AppConstants.VIEW_TYPE_IMAGE_MESSAGE_ME
                    else AppConstants.VIEW_TYPE_IMAGE_MESSAGE_OTHER
                } else if (message.type.toLowerCase().startsWith("video")) {
                    if (message.sender.userId.equals(SendBird.getCurrentUser().userId)) AppConstants.VIEW_TYPE_VIDEO_MESSAGE_ME
                    else AppConstants.VIEW_TYPE_VIDEO_MESSAGE_OTHER
                } else {
                    if (message.sender.userId.equals(SendBird.getCurrentUser().userId)) AppConstants.VIEW_TYPE_FILE_MESSAGE_ME
                    else AppConstants.VIEW_TYPE_FILE_MESSAGE_OTHER
                }
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

                isNewDay = !DateUtil.isSameDay(message.createdAt, previousMessage.createdAt)
            }
            (position == messages.size - 1) -> {
                isNewDay = true
            }
        }

        when (holder.itemViewType) {
            AppConstants.VIEW_TYPE_USER_MESSAGE_ME -> {
                holder as MyUserHolder
                holder.bindView(context, messages.get(position) as UserMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_USER_MESSAGE_OTHER -> {
                holder as OtherUserHolder
                holder.bindView(context, messages.get(position) as UserMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_IMAGE_MESSAGE_ME -> {
                holder as MyImageHolder
                holder.bindView(context, messages.get(position) as FileMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_IMAGE_MESSAGE_OTHER -> {
                holder as OtherImageHolder
                holder.bindView(context, messages.get(position) as FileMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_VIDEO_MESSAGE_ME -> {
                holder as MyVideoHolder
                holder.bindView(context, messages.get(position) as FileMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_VIDEO_MESSAGE_OTHER -> {
                holder as OtherVideoHolder
                holder.bindView(context, messages.get(position) as FileMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_FILE_MESSAGE_ME -> {
                holder as MyFileMessage
                holder.bindView(context, messages.get(position) as FileMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_FILE_MESSAGE_OTHER -> {
                holder as OtherFileMessage
                holder.bindView(context, messages.get(position) as FileMessage, isNewDay, listener)
            }
            AppConstants.VIEW_TYPE_ADMIN_MESSAGE -> {
                holder as AdminUserHolder
                holder.bindView(messages.get(position) as AdminMessage, isNewDay)
            }
            AppConstants.VIEW_TYPE_LOCATION_ME -> {
                holder as MyLocationHolder
                holder.bindView(context, messages.get(position) as UserMessage, isNewDay)
            }
            AppConstants.VIEW_TYPE_LOCATION_OTHER -> {
                holder as OtherLocationHolder
                holder.bindView(context, messages.get(position) as UserMessage, isNewDay)

            }
        }

    }

    class MyUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageText = view.text_gchat_message_me
        val date = view.text_gchat_date_me
        val messageDate = view.text_gchat_timestamp_me
        val read = view.text_gchat_read_me

        val separator = view.text_gchat_separator_me
        val urlContainer = view.layout_gchat_link_me
        val urlName = view.text_gchat_site_me
        val urlTitle = view.text_gchat_title_me
        val urlDescription = view.text_gchat_description_me
        val urlImage = view.image_gchat_url_me

        fun bindView(context: Context, message: UserMessage, isNewDay: Boolean, listener: OnItemClickListener) {

            messageText.setText(TextUtil.formatText(message.message), TextView.BufferType.SPANNABLE)
            messageDate.text = DateUtil.formatTime(message.createdAt)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            urlContainer.visibility = View.GONE
            if (message.customType.equals("url_preview")) {
                try {
                    urlContainer.visibility = View.VISIBLE
                    val obj = UrlInfo()
                    obj.setUrlInfo(message.data)
                    urlName.text = "@" + obj.siteName
                    urlTitle.text = obj.title
                    urlDescription.text = obj.description
                    Glide.with(context).load(obj.imageUrl).into(urlImage)
                    messageText.setText(
                        TextUtil.formatText(message.message.replace(obj.url, "")),
                        TextView.BufferType.SPANNABLE
                    )
                    if (messageText.text.equals("")) {
                        messageText.visibility = View.GONE
                        separator.visibility = View.GONE
                    }
                } catch (exception: JSONException) {
                    Log.e("JSON", exception.toString())
                }
            }

            itemView.setOnClickListener {
                listener.onUserMessageClick(message)
            }
        }
    }

    class OtherUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageText = view.text_gchat_message_other
        val date = view.text_gchat_date_other
        val timestamp = view.text_gchat_timestamp_other
        val read = view.text_gchat_read_other
        val profileImage = view.image_gchat_profile_other
        val user = view.text_gchat_user_other

        val separator = view.text_gchat_separator_other
        val urlContainer = view.layout_gchat_link_other
        val urlName = view.text_gchat_site_other
        val urlTitle = view.text_gchat_title_other
        val urlDescription = view.text_gchat_description_other
        val urlImage = view.image_gchat_url_other

        fun bindView(context: Context, message: UserMessage, isNewDay: Boolean, listener: OnItemClickListener) {

            messageText.setText(TextUtil.formatText(message.message), TextView.BufferType.SPANNABLE)

            timestamp.text = DateUtil.formatTime(message.createdAt)


            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            Glide.with(context).load(message.sender.profileUrl).apply(RequestOptions().override(75, 75))
                .into(profileImage)
            user.text = message.sender.nickname


            urlContainer.visibility = View.GONE
            if (message.customType.equals("url_preview")) {
                try {
                    urlContainer.visibility = View.VISIBLE
                    val obj = UrlInfo()
                    obj.setUrlInfo(message.data)
                    urlName.text = "@" + obj.siteName
                    urlTitle.text = obj.title
                    urlDescription.text = obj.description
                    Glide.with(context).load(obj.imageUrl).into(urlImage)
                    messageText.setText(
                        TextUtil.formatText(message.message.replace(obj.url, "")),
                        TextView.BufferType.SPANNABLE
                    )
                    // messageText.text = message.message.replace(obj.url, "")
                    if (messageText.text.equals("")) {
                        messageText.visibility = View.GONE
                        separator.visibility = View.GONE
                    }
                } catch (exception: JSONException) {
                    Log.e("JSON", exception.toString())
                }
            }

            itemView.setOnClickListener {
                listener.onUserMessageClick(message)
            }
        }

    }

    class OtherImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail = view.image_gchat_image_thumbnail_other
        val date = view.text_gchat_image_date_other
        val profileImage = view.image_gchat_image_profile_other
        val username = view.text_gchat_image_user_other
        val timestamp = view.text_gchat_image_timestamp_other
        val read = view.text_gchat_image_read_other


        fun bindView(context: Context, message: FileMessage, isNewDay: Boolean, listener: OnItemClickListener) {

            username.text = message.sender.nickname
            timestamp.text = DateUtil.formatTime(message.createdAt)

            Glide.with(context).load(message.sender.profileUrl).apply(RequestOptions().override(75, 75))
                .into(profileImage)
            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            val thumbnails = message.thumbnails

            if (thumbnails.size > 0) {
                if (message.type.toLowerCase().contains("gif")) {
                    ImageUtil.displayGifImageFromUrl(context, message.url, thumbnail, thumbnails.get(0).url)
                } else {
                    Glide.with(context).load(thumbnails.get(0).url).into(thumbnail) //TODO move into imageUtil
                }
            } else {
                Glide.with(context).load(message.url).into(thumbnail) //TODO move into imageUtil

            }

            itemView.setOnClickListener {
                listener.onFileMessageClicked(message)
            }

        }
    }

    class MyImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail = view.image_gchat_image_thumbnail_me
        val date = view.text_gchat_image_date_me
        val timestamp = view.text_gchat_image_timestamp_me
        val read = view.text_gchat_image_read_me

        fun bindView(context: Context, message: FileMessage, isNewDay: Boolean, listener: OnItemClickListener) {

            timestamp.text = DateUtil.formatTime(message.createdAt)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }


            val thumbnails = message.thumbnails

            if (thumbnails.size > 0) {
                if (message.type.toLowerCase().contains("gif")) {
                    ImageUtil.displayGifImageFromUrl(context, message.url, thumbnail, thumbnails.get(0).url)
                } else {
                    Glide.with(context).load(thumbnails.get(0).url).into(thumbnail)
                }
            }

            itemView.setOnClickListener {
                listener.onFileMessageClicked(message)
            }
        }
    }

    class MyVideoHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail = view.image_gchat_video_thumbnail_me
        val date = view.text_gchat_video_date_me
        val timestamp = view.text_gchat_video_timestamp_me
        val read = view.text_gchat_video_read_me


        fun bindView(context: Context, message: FileMessage, isNewDay: Boolean, listener: OnItemClickListener) {
            timestamp.text = DateUtil.formatTime(message.createdAt)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }


            val thumbnails = message.thumbnails

            if (thumbnails.size > 0) {
                Glide.with(context).load(thumbnails.get(0).url).into(thumbnail)
            }

            itemView.setOnClickListener {
                listener.onFileMessageClicked(message)
            }
        }
    }

    class OtherVideoHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail = view.image_gchat_video_thumbnail_other
        val date = view.text_gchat_video_date_other
        val profileImage = view.image_gchat_video_profile_other
        val username = view.text_gchat_video_user_other
        val timestamp = view.text_gchat_video_timestamp_other
        val read = view.text_gchat_video_read_other

        fun bindView(context: Context, message: FileMessage, isNewDay: Boolean, listener: OnItemClickListener) {
            username.text = message.sender.nickname
            timestamp.text = DateUtil.formatTime(message.createdAt)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            Glide.with(context).load(message.sender.profileUrl).apply(RequestOptions().override(75, 75))
                .into(profileImage)


            val thumbnails = message.thumbnails

            if (thumbnails.size > 0) {
                Glide.with(context).load(thumbnails.get(0).url).into(thumbnail)

            }

            itemView.setOnClickListener {
                listener.onFileMessageClicked(message)
            }

        }
    }

    class MyFileMessage(view: View) : RecyclerView.ViewHolder(view) {
        val filename = view.text_gchat_filename_me
        val date = view.text_gchat_file_date_me
        val timestamp = view.text_gchat_file_timestamp_me
        val read = view.text_gchat_file_read_me

        fun bindView(context: Context, message: FileMessage, isNewDay: Boolean, listener: OnItemClickListener) {

            filename.text = message.name
            timestamp.text = DateUtil.formatTime(message.createdAt)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            itemView.setOnClickListener {
                listener.onFileMessageClicked(message)
            }

        }
    }

    class OtherFileMessage(view: View) : RecyclerView.ViewHolder(view) {
        val filename = view.text_gchat_filename_me
        val profileImage = view.image_gchat_file_profile_other
        val username = view.text_gchat_file_user_other
        val date = view.text_gchat_file_date_other
        val timestamp = view.text_gchat_timestamp_other
        val read = view

        fun bindView(context: Context, message: FileMessage, isNewDay: Boolean, listener: OnItemClickListener) {

            filename.text = message.name
            username.text = message.sender.nickname
            timestamp.text = DateUtil.formatTime(message.createdAt)

            Glide.with(context).load(message.sender.profileUrl).apply(RequestOptions().override(75, 75))
                .into(profileImage)

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            itemView.setOnClickListener {
                listener.onFileMessageClicked(message)
            }

        }
    }


    class AdminUserHolder(view: View) : RecyclerView.ViewHolder(view) {

        val messageText = view.text_gchat_admin
        val date = view.text_gchat_date

        fun bindView(message: AdminMessage, isNewDay: Boolean) {

            messageText.text = message.message

            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

        }
    }

    class MyLocationHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
        private lateinit var map: GoogleMap
        private lateinit var context: Context
        private lateinit var latLng: LatLng
        private var longitude = 0.0
        private var latitude = 0.0
        private val mapView = view.map_gchat_me
        private val date = view.text_gchat_map_date_me
        private val timestamp = view.text_gchat_map_timestamp_me


        /** Initialises the MapView by calling its lifecycle methods */
        init {
            with(mapView) {
                // Initialise the MapView
                onCreate(null)
                // Set the map ready callback to receive the GoogleMap object
                getMapAsync(this@MyLocationHolder)
            }
        }

        fun bindView(context: Context, message: UserMessage, isNewDay: Boolean) {
            this.context = context
            timestamp.text = DateUtil.formatTime(message.createdAt)
            val cords = message.message.split(",")

            longitude = cords[0].toDouble()
            latitude = cords[1].toDouble()

            latLng = LatLng(latitude, longitude)
            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }

            setLocation()

        }

        override fun onMapReady(googleMap: GoogleMap?) {
            MapsInitializer.initialize(context.applicationContext)
            map = googleMap ?: return
            setLocation()

        }

        private fun setLocation() {
            if (!::map.isInitialized) return
            with(map) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                addMarker(MarkerOptions().position(latLng))
                mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            mapView.onResume()
        }
    }

    class OtherLocationHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
        private lateinit var map: GoogleMap
        private lateinit var context: Context
        private lateinit var latLng: LatLng
        private var longitude = 0.0
        private var latitude = 0.0
        private val mapView = view.map_gchat_other
        private val date = view.text_gchat_map_date_other
        private val timestamp = view.text_gchat_map_timestamp_other
        private val profileImage = view.image_gchat_map_profile_other
        private val username = view.text_gchat_map_user_other


        /** Initialises the MapView by calling its lifecycle methods */
        init {
            with(mapView) {
                // Initialise the MapView
                onCreate(null)
                // Set the map ready callback to receive the GoogleMap object
                getMapAsync(this@OtherLocationHolder)
            }
        }

        fun bindView(context: Context, message: UserMessage, isNewDay: Boolean) {
            this.context = context
            val cords = message.message.split(",")

            longitude = cords[0].toDouble()
            latitude = cords[1].toDouble()

            latLng = LatLng(latitude, longitude)

            username.text = message.sender.nickname
            timestamp.text = DateUtil.formatTime(message.createdAt)
            Glide.with(context).load(message.sender.profileUrl).apply(RequestOptions().override(75, 75))
                .into(profileImage)
            if (isNewDay) {
                date.visibility = View.VISIBLE
                date.text = DateUtil.formatDate(message.createdAt)
            } else {
                date.visibility = View.GONE
            }
            setLocation()

        }

        override fun onMapReady(googleMap: GoogleMap?) {
            MapsInitializer.initialize(context.applicationContext)
            map = googleMap ?: return
            setLocation()

        }

        private fun setLocation() {
            if (!::map.isInitialized) return
            with(map) {
                moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                addMarker(com.google.android.gms.maps.model.MarkerOptions().position(latLng))
                mapType = com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
            }
            mapView.onResume()
        }
    }
}