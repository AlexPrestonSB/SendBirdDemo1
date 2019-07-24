package com.sendbirdsampleapp.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

object ImageUtil {


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


    fun displayGifFromUrl(context: Context, url: String, imageView: ImageView, listener: RequestListener<GifDrawable>) {
        val myOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        Glide.with(context).asGif().load(url).apply(myOptions).listener(listener).into(imageView)
    }

    fun displayImageFromUrl(context: Context, url: String, imageView: ImageView, listener: RequestListener<Drawable>) {
        val myOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        Glide.with(context).load(url).apply(myOptions).listener(listener).into(imageView)
    }
}