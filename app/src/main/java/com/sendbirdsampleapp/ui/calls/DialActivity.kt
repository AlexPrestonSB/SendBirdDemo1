package com.sendbirdsampleapp.ui.calls

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sendbird.calls.DirectCall
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.handler.SendBirdCallListener
import com.sendbirdsampleapp.R
import kotlinx.android.synthetic.main.activity_dial.*
import java.util.*

class DialActivity : AppCompatActivity() {

    private val CALLEE_ID = "CALLEE_ID"
    private val CALL_TYPE = "IS_VIDEO"
    private val INCOMMING = "INCOMMING"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dial)

        val s = SendBirdCall.getCurrentUser().isActive



        image_video_call.setOnClickListener { setVideoListener() }
        image_voice_call.setOnClickListener { setVoiceListener() }

        SendBirdCall.addListener(
            UUID.randomUUID().toString(),
            object : SendBirdCallListener() {
                override fun onRinging(call: DirectCall) {
                    Log.d(
                        "TAG",
                        "onRinging() => callId: " + call.callId
                    )
//                        if (CallActivity.isRunning) {
//                            call.end()
//                            return
//                        }

//                    if (call.isVideoCall) {
//                        val intent = Intent(applicationContext, VideoCallActivity::class.java)
//                        intent.putExtra(CALLEE_ID, call.callee.userId)
//                        intent.putExtra(CALL_TYPE, true)
//                        intent.putExtra(CALL_ID, call.callId)
//                        intent.putExtra(INCOMMING, true)
//                        startActivity(intent)
//                    } else {
//                        val intent = Intent(applicationContext, VoiceCallActivity::class.java)
//                        intent.putExtra(CALLEE_ID, call.callee.userId)
//                        intent.putExtra(CALL_ID, call.callId)
//                        intent.putExtra(CALL_TYPE, false)
//                        intent.putExtra(INCOMMING, true)
//                        startActivity(intent)
//                    }
                }
            })


    }

    fun setVoiceListener() {
        val calleID = text_dial_userid.text.toString()

        if(!calleID.isEmpty()) {
            val intent = Intent(this, VoiceCallActivity::class.java)
            intent.putExtra(CALLEE_ID, calleID)
            intent.putExtra(CALL_TYPE, false)
            intent.putExtra(INCOMMING, false)
            startActivity(intent)
        }

    }

    fun setVideoListener() {

        val calleID = text_dial_userid.text.toString()

        if(!calleID.isEmpty()) {
            val intent = Intent(this, VideoCallActivity::class.java)
            intent.putExtra(CALLEE_ID, calleID)
            intent.putExtra(CALL_TYPE, true)
            intent.putExtra(INCOMMING, false)
            startActivity(intent)
        }

    }
}