package com.fitlinks.message.android.Message.Model

/**
 * Created by venu on 22-09-2017.
 */
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.sql.Timestamp
import java.util.*

/**
 * Created by venu on 22-09-2017.
 */

data class MessageModel(
        val msgId: String = "",
        val ownerImg: String = "",
        val receiverId: String = "",
        val ownerName: String = "",
        val receiverName: String = "",
        val updatedTimeStamp: Long = -1,
        val receiverImg: String = "",
        val message: String = "",
        val file: String = "",
        val image: String? = null,
        val ownerId: String = "",
        val channelId: String = "",
        @Exclude
        var isOutComing: Boolean = false
) : IMessage, MessageContentType, MessageContentType.Image {


    override fun getId(): String {
        return msgId
    }

    //returns time of creation
    override fun getCreatedAt(): Date {
        val timestamp: Timestamp
        if (updatedTimeStamp > 0)
            timestamp = Timestamp(updatedTimeStamp)
        else timestamp = Timestamp(Date().time)
        return Date(timestamp.time)
    }

    //returns owner info
    override fun getUser(): IUser {
        return InvitieResponse(ownerId, ownerImg, ownerName)
    }

    //returns msg content
    override fun getText(): String {
        return message
    }

    //doesnt return image
    override fun getImageUrl(): String? {
        return null
    }

}