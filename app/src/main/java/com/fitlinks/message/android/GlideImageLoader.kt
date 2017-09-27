package com.fitlinks




import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.stfalcon.chatkit.commons.ImageLoader
import java.net.URI

/**
 * Created by aditlal on 12/07/17.
 */
class GlideImageLoader : ImageLoader {
    override fun loadImage(imageView: ImageView, url: String) {
        if(url.contains("http"))
            Glide.with(imageView!!.context).load(url!!).into(imageView)
        else if(url.contains("content")){
            val bitmap = MediaStore.Images.Media.getBitmap(imageView.context.contentResolver, Uri.parse(url))
            imageView.setImageBitmap(bitmap)
        }
    }
}
