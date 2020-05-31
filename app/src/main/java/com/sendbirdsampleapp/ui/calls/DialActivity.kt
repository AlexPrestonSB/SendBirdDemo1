package com.sendbirdsampleapp.ui.calls

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sendbirdsampleapp.R
import kotlinx.android.synthetic.main.activity_dial.*

class DialActivity : AppCompatActivity() {

    private val CALLEE_ID = "CALLEE_ID"
    private val CALL_TYPE = "IS_VIDEO"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dial)

        image_video_call.setOnClickListener { setVideoListener() }
        image_voice_call.setOnClickListener { setVoiceListener() }

    }

    fun setVoiceListener() {
        val calleID = text_dial_userid.text.toString()

        if(!calleID.isEmpty()) {
            val intent = Intent(this, VoiceCallActivity::class.java)
            intent.putExtra(CALLEE_ID, calleID)
            intent.putExtra(CALL_TYPE, false)
            startActivity(intent)
        }

    }

    fun setVideoListener() {

        val calleID = text_dial_userid.text.toString()

        if(!calleID.isEmpty()) {
            val intent = Intent(this, VideoCallActivity::class.java)
            intent.putExtra(CALLEE_ID, calleID)
            intent.putExtra(CALL_TYPE, true)
            startActivity(intent)
        }

    }
}