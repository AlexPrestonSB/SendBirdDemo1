package com.sendbirdsampleapp.ui.calls

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sendbird.calls.*
import com.sendbird.calls.handler.DialHandler
import com.sendbird.calls.handler.DirectCallListener
import com.sendbirdsampleapp.R
import kotlinx.android.synthetic.main.activity_voice_call.*
import kotlin.math.log

class VoiceCallActivity : AppCompatActivity() {

    private val CALLEE_ID = "CALLEE_ID"
    private val CALL_TYPE = "IS_VIDEO"
    private val INCOMMING = "INCOMMING"
    private val CALL_ID = "CALL_ID"
    private lateinit var calleeId: String
    private var isVideo: Boolean = false
    private var isIncoming: Boolean = false
    private lateinit var callId: String
    private lateinit var call: DirectCall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_call)

        calleeId = intent.getStringExtra(CALLEE_ID)
        isVideo = intent.getBooleanExtra(CALL_TYPE, false)
        isIncoming = intent.getBooleanExtra(INCOMMING, false)



        if (isIncoming) {
            callId = intent.getStringExtra(CALL_ID)
            call = SendBirdCall.getCall(callId)

            handleIncomingCall()
        } else {
            dial()
        }


        image_voice_accept.setOnClickListener { acceptListener() }
        image_voice_decline.setOnClickListener { declineListener() }
    }

    fun handleIncomingCall() {


    }

    fun dial() {
        val dParams = DialParams(calleeId)
        dParams.setVideoCall(isVideo)
        dParams.setCallOptions(CallOptions())

        call = SendBirdCall.dial(dParams, object : DialHandler {
            override fun onResult(callInfo: DirectCall?, e: SendBirdException?) {
                if (e == null) {
                    Log.d("TAG", callInfo?.callId)
                }
            }
        })

        call.setListener(object : DirectCallListener() {
            override fun onEnded(p0: DirectCall?) {
                redirectToDial()
            }

            override fun onConnected(p0: DirectCall?) {
            }
        })
    }

    fun acceptUI() {
        image_voice_accept.visibility
    }

    fun declineListener() {
        if (callId.isNotEmpty()) {
            call.end()
            redirectToDial()
        }
    }

    fun acceptListener() {
        if (callId.isNotEmpty()) {
            call.accept(AcceptParams())
        }
    }

    fun redirectToDial() {
        val intent = Intent(this, DialActivity::class.java)
        startActivity(intent)
    }




}