package com.sendbirdsampleapp.data.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log
import com.google.android.gms.common.util.SharedPreferencesUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sendbird.android.SendBird.registerPushTokenForCurrentUser
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdException
import com.sendbirdsampleapp.R
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.ui.group_channel.chat_group.GroupChannelChatActivity
import com.sendbirdsampleapp.util.PushUtil
import org.json.JSONObject


class FirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FIREBASE_MESSAGING"
    private val EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (SendBirdCall.handleFirebaseMessageData(remoteMessage.data)) {
            Log.d("TAG", remoteMessage.data.toString())
        } else {
            val message = remoteMessage?.data?.get("message")
            val sendbird = JSONObject(remoteMessage?.data?.get("sendbird"))
            val channel = sendbird.get("channel") as JSONObject
            val channelUrl = channel.get("channel_url").toString()
            message?.let { sendNotification(it, channelUrl) }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val user = SendBirdCall.getCurrentUser()
        if (SendBirdCall.getCurrentUser() != null) {
            PushUtil.registerCallPushToken(applicationContext, token, pushTokenHandler)
        } else {

            AppPreferenceHelper(this).setCallToken(token)
            registerPushTokenForCurrentUser(token) { pushTokenRegistrationStatus, sendBirdException ->
                if (sendBirdException != null) {
                    Log.e(TAG, "Failed to register token")
                }

                Log.d(TAG, "Successful registration")
            }
        }

    }

    private val pushTokenHandler = object : PushUtil.PushTokenHandler {
        override fun onResult(e: SendBirdException?) {
            if (e != null) {
                Log.e("TAG", e.localizedMessage)
            }
        }
    }

    private fun sendNotification(message: String, channelUrl: String?) {
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
            .setContentTitle(this.getResources().getString(R.string.app_name))
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        builder.setContentText(message)

        notificationManager.notify(0, builder.build())
    }


}