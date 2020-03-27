package com.sendbirdsampleapp.ui.group_channel.chat_group

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.sendbird.android.*
import com.sendbirdsampleapp.BaseApp
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.ui.group_channel.chat_group.presenter.GroupChannelChatPresenterImpl
import com.sendbirdsampleapp.ui.group_channel.chat_group.view.GroupChannelChatView
import com.sendbirdsampleapp.ui.group_channel.list_group.GroupChannelActivity
import com.sendbirdsampleapp.util.MediaPlayerActivity
import com.sendbirdsampleapp.util.PhotoViewerActivity
import kotlinx.android.synthetic.main.activity_gchat.*
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class GroupChannelChatActivity : AppCompatActivity(), GroupChannelChatView,
    GroupChannelChatAdapter.OnItemClickListener {

    private val EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL"
    private val INTENT_REQUEST_CHOOSE_MEDIA = 301


    @Inject
    lateinit var presenter: GroupChannelChatPresenterImpl

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: GroupChannelChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gchat)

        BaseApp.app(this).injector.inject(this)

        presenter.setView(this)

        setListeners()
        setUpRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this, getChannelURl())
    }

    override fun onPause() {
        super.onPause()
        presenter.setTypingStatus(false)
        presenter.onPause()
    }


    override fun typingIndicator(message: String) {
        recyclerView.scrollToPosition(0)
        if (message != "") {
            text_gchat_indicator.visibility = View.VISIBLE
            text_gchat_indicator.setText(message)
        } else {
            text_gchat_indicator.setText(message)
            text_gchat_indicator.visibility = View.GONE
        }
    }


    override fun showValidationMessage(errorCode: Int) {
        Log.e("TEST", errorCode.toString())
    }

    override fun backPressed() {
        val intent = Intent(this, GroupChannelActivity::class.java)
        startActivity(intent)

    }

    override fun displayChatTitle(title: String) {
        text_gchat_name.text = title
    }

    override fun displayPushNotification(message: UserMessage, channelUrl: String?) {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val CHANNEL_ID = "CHANNEL_ID"
        if (Build.VERSION.SDK_INT >= 26) {
            val channel =
                NotificationChannel(CHANNEL_ID, "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, GroupChannelChatActivity::class.java)
        intent.putExtra(EXTRA_CHANNEL_URL, channelUrl)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_notification)
            .setColor(Color.parseColor("#7469C4"))
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.drawable.img_notification_large
                )
            )
            .setContentTitle(this.resources.getString(R.string.app_name))
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        builder.setContentText(message.message)

        notificationManager.notify(0, builder.build())
    }

    override fun selectMedia(intent: Intent) {
        startActivityForResult(
            Intent.createChooser(intent, "Select Media"),
            INTENT_REQUEST_CHOOSE_MEDIA
        )
        SendBird.setAutoBackgroundDetection(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        SendBird.setAutoBackgroundDetection(true)

        if (requestCode == INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            presenter.sendMessageThumbnail(this, data.data)
        }
    }

    override fun onUserMessageClick(message: UserMessage) {
        if (message.customType.equals("url_preview")) {
            try {
                val obj = JSONObject(message.data)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("url")))
                startActivity(intent)
            } catch (exception: JSONException) {
                Log.e("JSON", exception.toString())
            }

        }
    }

    override fun onFileMessageClicked(message: FileMessage) {
        when {
            message.type.toLowerCase().startsWith("video") -> {
                val intent = Intent(this, MediaPlayerActivity::class.java)
                intent.putExtra("url", message.url)
                intent.putExtra("name", message.name)
                startActivity(intent)
            }
            message.type.toLowerCase().startsWith("image") -> {
                val intent = Intent(this, PhotoViewerActivity::class.java)
                intent.putExtra("url", message.url)
                intent.putExtra("type", message.type)
                startActivity(intent)
            }
            else -> {
                //TODO
            }
        }
    }

    override fun searchMessages(messages: MutableList<BaseMessage>) {
        adapter.loadMessages(messages)

    }

    override fun addFirst(message: BaseMessage) {
        adapter.addFirst(message)
        edit_gchat_message.text.clear()
    }

    override fun loadMessages(messages: MutableList<BaseMessage>, search: Boolean) {
        adapter.loadMessages(messages)
        if (!search) {
            recyclerView.scrollToPosition(0)
        } else {
            recyclerView.scrollToPosition(messages.size)
        }

    }

    override fun clear() {
        message_search_messages.text.clear()
        adapter.clear()
    }

    private fun setListeners() {
        button_gchat_back.setOnClickListener { presenter.backPressed() }
        button_gchat_send.setOnClickListener { presenter.sendMessage(edit_gchat_message.text.toString()) }
        button_gchat_upload.setOnClickListener { presenter.requestMedia() }

        clear_search_messages.setOnClickListener {
            presenter.clear()
        }
        search_button_messages.setOnClickListener {
            if (message_search_messages.text.isNotEmpty()) {
                presenter.messageSearch(message_search_messages.text.toString())
            }
        }

        edit_gchat_message.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recyclerView.scrollToPosition(0)
                if (s?.isNotEmpty()!!) {
                    presenter.setTypingStatus(true)
                } else {
                    presenter.setTypingStatus(false)
                }

            }
        })
    }

    private fun setUpRecyclerView() {
        adapter = GroupChannelChatAdapter(this, this)
        recyclerView = recycler_gchat
        recyclerView.adapter = adapter
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager
        recyclerView.scrollToPosition(0)

    }

    private fun getChannelURl(): String {
        val intent = this.intent
        return intent.getStringExtra(EXTRA_CHANNEL_URL)
    }
}