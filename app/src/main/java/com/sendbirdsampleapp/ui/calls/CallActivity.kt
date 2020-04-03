package com.sendbirdsampleapp.ui.calls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CallActivity : AppCompatActivity() {

    companion object {
        var isRunning = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRunning = true
    }


}