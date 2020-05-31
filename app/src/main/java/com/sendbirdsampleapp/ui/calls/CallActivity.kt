package com.sendbirdsampleapp.ui.calls

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sendbird.calls.DirectCall
import com.sendbird.calls.DirectCallEndResult
import com.sendbirdsampleapp.R
import java.util.*

class CallActivity : AppCompatActivity() {

    companion object {
        var isRunning = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRunning = true


    }



    private fun getEndResultString(call: DirectCall?): String? {
        var endResultString: String? = ""
        if (call != null) {
            when (call.endResult) {
                DirectCallEndResult.NONE -> {
                }
                DirectCallEndResult.NO_ANSWER -> endResultString =
                    getString(R.string.calls_end_result_no_answer)
                DirectCallEndResult.CANCELED -> endResultString =
                    getString(R.string.calls_end_result_canceled)
                DirectCallEndResult.DECLINED -> endResultString =
                    getString(R.string.calls_end_result_declined)
                DirectCallEndResult.COMPLETED -> endResultString =
                    getString(R.string.calls_end_result_completed)
                DirectCallEndResult.TIMED_OUT -> endResultString =
                    getString(R.string.calls_end_result_timed_out)
                DirectCallEndResult.CONNECTION_LOST -> endResultString =
                    getString(R.string.calls_end_result_connection_lost)
                DirectCallEndResult.UNKNOWN -> endResultString =
                    getString(R.string.calls_end_result_unknown)
                DirectCallEndResult.DIAL_FAILED -> endResultString =
                    getString(R.string.calls_end_result_dial_failed)
                DirectCallEndResult.ACCEPT_FAILED -> endResultString =
                    getString(R.string.calls_end_result_accept_failed)
                DirectCallEndResult.OTHER_DEVICE_ACCEPTED -> endResultString =
                    getString(R.string.calls_end_result_other_device_accepted)
            }
        }
        return endResultString
    }


    override fun onBackPressed() {}


    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }


}