package com.fitlinks.message.android.model

import com.google.firebase.database.Exclude
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import model.InvitieResponse

import java.sql.Timestamp
import java.util.*

data class MessageModel(
        var msgId: String = "",
        var ownerImg: String = "",
        var receiverId: String = "",
        var ownerName: String = "",
        var receiverName: String = "",
        var updatedTimeStamp: Long = -1,
        var receiverImg: String = "",
        var message: String = "",
        var image: String? = null,
        var ownerId: String = "",
        var channelId: String = "",
        @Exclude
        var isOutComing: Boolean = false
) : IMessage, MessageContentType, MessageContentType.Image {


    override fun getId(): String {
        return msgId
    }

    override fun getCreatedAt(): Date {
        val timestamp: Timestamp
        if (updatedTimeStamp > 0)
            timestamp = Timestamp(updatedTimeStamp)
        else timestamp = Timestamp(Date().time)
        return Date(timestamp.time)
    }

    override fun getUser(): IUser {
        return InvitieResponse(ownerId, ownerImg, ownerName)
    }

    override fun getText(): String {
        return message
    }

    override fun getImageUrl(): String? {
        return image
    }

}
