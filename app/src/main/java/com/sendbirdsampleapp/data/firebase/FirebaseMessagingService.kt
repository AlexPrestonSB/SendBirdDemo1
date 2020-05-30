package com.sendbirdsampleapp.data.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sendbird.android.SendBird.registerPushTokenForCurrentUser
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdException
import com.sendbirdsampleapp.data.preferences.AppPreferenceHelper
import com.sendbirdsampleapp.util.PushUtil


class FirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (SendBirdCall.handleFirebaseMessageData(remoteMessage.data)) {
            Log.d(
                "TAG",
                "[SendBirdCall Message] onMessageReceived() => " + remoteMessage.data.toString()
            )
        } else {
            Log.d(
                "TAG",
                "onMessageReceived() => From: " + remoteMessage.from
            )
            if (remoteMessage.data.size > 0) {
                Log.d(
                    "TAG",
                    "onMessageReceived() => Data: " + remoteMessage.data.toString()
                )
            }
            if (remoteMessage.notification != null) {
                Log.d(
                    "TAG",
                    "onMessageReceived() => Notification Body: " + remoteMessage.notification!!.body
                )
            }
        }
    }

    override fun onNewToken(token: String) {

        if (SendBirdCall.getCurrentUser() != null) {
            PushUtil.registerCallPushToken(applicationContext, token, object : PushUtil.PushTokenHandler{
                override fun onResult(e: SendBirdException?) {
                    if(e != null) {
                        Log.e("TAG", "Failed to register token")
                    }
                }
            })
        } else {

            AppPreferenceHelper(this).setCallToken(token)
        }

    }
//
//    private val pushTokenHandler = object : PushUtil.PushTokenHandler {
//        override fun onResult(e: SendBirdException?) {
//            if (e != null) {
//                Log.e("TAG", e.localizedMessage)
//            }
//        }
//    }
//
//    private fun sendNotification(message: String, channelUrl: String?) {
//        val notificationManager =
//            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val CHANNEL_ID = "CHANNEL_ID"
//        if (Build.VERSION.SDK_INT >= 26) {
//            val channel =
//                NotificationChannel(CHANNEL_ID, "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val intent = Intent(this, GroupChannelChatActivity::class.java)
//        intent.putExtra(EXTRA_CHANNEL_URL, channelUrl)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val builder = NotificationCompat
//            .Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.img_notification)
//            .setColor(Color.parseColor("#7469C4"))
//            .setLargeIcon(
//                BitmapFactory.decodeResource(
//                    this.resources,
//                    R.drawable.img_notification_large
//                )
//            )
//            .setContentTitle(this.getResources().getString(R.string.app_name))
//            .setAutoCancel(true)
//            .setPriority(Notification.PRIORITY_MAX)
//            .setDefaults(Notification.DEFAULT_ALL)
//            .setContentIntent(pendingIntent)
//
//        builder.setContentText(message)
//
//        notificationManager.notify(0, builder.build())
//    }


}