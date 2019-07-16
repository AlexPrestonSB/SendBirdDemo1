package com.sendbirdsampleapp.util

import android.app.Activity
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewTreeObserver
import com.sendbirdsampleapp.R
import kotlinx.android.synthetic.main.activity_media_player.*
import java.lang.Exception

class MediaPlayerActivity : Activity(), SurfaceHolder.Callback, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnVideoSizeChangedListener {


    private lateinit var url: String
    private lateinit var name: String

    private var width = 0
    private var height = 0;

    private var sizeKnown = false
    private var isReady = false
    private var containerSizeKnown = false

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var surfaceView: SurfaceView
    private lateinit var container: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        surfaceView = surface_view_media_player
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)

        val bundle = intent.extras
        if (bundle != null) {
            url = bundle.getString("url")
            name = bundle.getString("name")
        }

        progress_bar_media_buffering.visibility = View.VISIBLE
        container = media_player_layout
        setContainerLayoutListener(false)
    }

    private fun setContainerLayoutListener(screenRotated: Boolean) {
        container.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                container.viewTreeObserver.removeOnGlobalLayoutListener(this)

                containerSizeKnown = true
                if (screenRotated) {
                    setVideoSize()
                } else {
                    tryPlayback()
                }
            }
        })
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        play()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        finish()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        isReady = true
        tryPlayback()
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
    }

    override fun onVideoSizeChanged(mp: MediaPlayer?, width: Int, height: Int) {
        if (width == 0 || height == 0) {
            return
        }

        this.width = width
        this.height = height
        sizeKnown = true
        tryPlayback()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        cleanUp()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setContainerLayoutListener(true)
    }

    private fun startPlayback() {
        progress_bar_media_buffering.visibility = View.INVISIBLE
        if (!mediaPlayer.isPlaying) {
            surfaceHolder.setFixedSize(width, height)
            setVideoSize()

            mediaPlayer.start()
        }
    }

    private fun tryPlayback() {
        if (isReady && sizeKnown && containerSizeKnown) {
            startPlayback()
        }
    }

    private fun setVideoSize() {
        try {
            val videoWidth = mediaPlayer.videoWidth
            val videoHeight = mediaPlayer.videoHeight
            val videoProportion = videoWidth.toFloat() / videoHeight.toFloat()

            val videoWidthInContainer = container.getWidth()
            val videoHeightInContainer = container.getHeight()
            val videoInContainerProportion = videoWidthInContainer.toFloat() / videoHeightInContainer.toFloat()

            val lp = surfaceView.getLayoutParams()
            if (videoProportion > videoInContainerProportion) {
                lp.width = videoWidthInContainer
                lp.height = (videoWidthInContainer.toFloat() / videoProportion).toInt()
            } else {
                lp.width = (videoProportion * videoHeightInContainer.toFloat()).toInt()
                lp.height = videoHeightInContainer
            }
            surfaceView.layoutParams = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun cleanUp() {
        width = 0
        height = 0

        isReady = false
        sizeKnown = false
    }

    private fun play() {
        progress_bar_media_buffering.visibility = View.VISIBLE
        cleanUp()
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setDisplay(surfaceHolder)
            mediaPlayer.setOnBufferingUpdateListener(this)
            mediaPlayer.setOnCompletionListener(this)
            mediaPlayer.setOnVideoSizeChangedListener(this)
            mediaPlayer.setOnPreparedListener(this)
        } catch (e: Exception) {
            Log.e("MEDIA_PLAYER", e.printStackTrace().toString())
        }
    }
}