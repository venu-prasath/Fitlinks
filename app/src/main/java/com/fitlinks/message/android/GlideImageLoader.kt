package com.fitlinks




import android.widget.ImageView
import com.bumptech.glide.Glide
import com.stfalcon.chatkit.commons.ImageLoader

/**
 * Created by aditlal on 12/07/17.
 */
class GlideImageLoader : ImageLoader {
    override fun loadImage(imageView: ImageView?, url: String?) {
        Glide.with(imageView!!.context).load(url!!).into(imageView)
    }
}
