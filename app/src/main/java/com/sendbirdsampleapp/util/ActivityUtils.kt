package com.sendbirdsampleapp.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.sendbird.calls.DirectCall
import com.sendbirdsampleapp.ui.calls.VideoCallActivity
import com.sendbirdsampleapp.ui.calls.VoiceCallActivity

class ActivityUtils {

    companion object {
        val EXTRA_INCOMING_CALL_ID = "incoming_call_id"
        val EXTRA_CALLEE_ID = "callee_id"
        val EXTRA_IS_VIDEO_CALL = "is_video_call"
    }


    fun startCallActivityAsCallee(
        context: Context,
        call: DirectCall
    ) {
        Log.d("TAG", "startCallActivityAsCallee()")
        val intent: Intent
        intent = if (call.isVideoCall) {
            Intent(context, VideoCallActivity::class.java)
        } else {
            Intent(context, VoiceCallActivity::class.java)
        }
        intent.putExtra(EXTRA_INCOMING_CALL_ID, call.callId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(intent)
    }

}