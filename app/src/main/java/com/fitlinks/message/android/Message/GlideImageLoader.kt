package com.fitlinks.message.android.Message

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.stfalcon.chatkit.commons.ImageLoader

/**
 * Created by aditlal on 12/07/17.
 * Taken from AOTM-reference
 */
class GlideImageLoader : ImageLoader {
    override fun loadImage(imageView: ImageView?, url: String?) {
        Glide.with(imageView!!.context).load(url!!).into(imageView)
    }
}
