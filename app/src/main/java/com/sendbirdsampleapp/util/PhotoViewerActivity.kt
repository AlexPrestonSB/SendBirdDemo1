package com.sendbirdsampleapp.util

import android.app.Activity
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sendbirdsampleapp.R
import kotlinx.android.synthetic.main.activity_image_displayer.*

class PhotoViewerActivity : Activity() {

    private lateinit var image: ImageView
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_displayer)

        var url = ""
        var type = ""

        val bundle = intent.extras
        if (bundle != null) {
            url = bundle.getString("url")
            type = bundle.getString("type")
        }

        image = image_view_photo
        progress = progress_bar_image_buffering

        if (type != "" && type.toLowerCase().contains("gif")) {
            ImageUtil.displayGifFromUrl(this, url, image, object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.INVISIBLE
                    return false
                }
            })
        } else {
            ImageUtil.displayImageFromUrl(this, url, image, object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.INVISIBLE
                    return false
                }
            })
        }

    }

}