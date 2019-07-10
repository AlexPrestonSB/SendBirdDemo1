package com.sendbirdsampleapp.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object MediaUtil {

    /**
     * Displays an GIF image from a URL in an ImageView.
     */
    fun displayGifImageFromUrl(
        context: Context,
        url: String,
        imageView: ImageView,
        thumbnailUrl: String?
    ) {
        val myOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        if (thumbnailUrl != null) {
            Glide.with(context)
                .asGif()
                .load(url)
                .apply(myOptions)
                .thumbnail(Glide.with(context).asGif().load(thumbnailUrl))
                .into(imageView)
        } else {
            Glide.with(context)
                .asGif()
                .load(url)
                .apply(myOptions)
                .into(imageView)
        }
    }
}