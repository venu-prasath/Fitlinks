package com.fitlinks.message.android.model

import com.google.firebase.database.Exclude
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import model.InvitieResponse

import java.sql.Timestamp
import java.util.*

data class MessageModel(
        val msgId: String = "",
        val ownerImg: String = "",
        val receiverId: String = "",
        val ownerName: String = "",
        val receiverName: String = "",
        val updatedTimeStamp: Long = -1,
        val receiverImg: String = "",
        val message: String = "",
        val image: String? = null,
        val ownerId: String = "",
        val channelId: String = "",
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
        return null
    }

}
