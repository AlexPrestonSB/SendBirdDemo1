package com.sendbirdsampleapp.util

import com.sendbird.android.SendBird
import com.sendbird.syncmanager.SendBirdSyncManager

object ConnectionUtil {

    fun addConnectionManagementHandler(handlerId: String, userId: String, handler: ConnectionManagementHandler){
        SendBird.addConnectionHandler(handlerId, object: SendBird.ConnectionHandler {
            override fun onReconnectStarted() {
               SendBirdSyncManager.getInstance().pauseSync()
            }

            override fun onReconnectSucceeded() {
                SendBirdSyncManager.getInstance().resumeSync()
                handler?.onConnected(true)
            }

            override fun onReconnectFailed() {
            }
        })

        if (SendBird.getConnectionState() == SendBird.ConnectionState.OPEN) {
            if (handler != null) {
                handler.onConnected(false)
            }
        } else if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED) {
            SendBird.connect(userId) { user, e ->
                if (e != null) {
                    return@connect
                }
                SendBirdSyncManager.getInstance().resumeSync()
                if (handler != null) {
                    handler.onConnected(false)
                }
            }
        }
    }

    fun removeConnectionManagementHandler(handlerId: String) {
        SendBird.removeConnectionHandler(handlerId)
    }

    interface ConnectionManagementHandler {
        fun onConnected(connected: Boolean)
    }
}