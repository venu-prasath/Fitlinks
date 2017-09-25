package com.fitlinks.message.android.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Nasi on 22/09/17.
 */



data class MessageDialogModel(
        var trainerName: String = "",
        var buddyName: String = "",
        var trainerEmail: String = "",
        var buddyEmail: String = "",
        var trainerImg: String = "",
        var buddyImg: String = "",
        var updatedTimeStamp: Long = 0,
        var last_chat: String = "",
        var last_user: String = "",
        var channelId: String = "",
        var currentUserTrainer: Boolean = false
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(trainerName)
        writeString(buddyName)
        writeString(trainerEmail)
        writeString(buddyEmail)
        writeString(trainerImg)
        writeString(buddyImg)
        writeLong(updatedTimeStamp)
        writeString(last_chat)
        writeString(last_user)
        writeString(channelId)
        writeInt((if (currentUserTrainer) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MessageDialogModel> = object : Parcelable.Creator<MessageDialogModel> {
            override fun createFromParcel(source: Parcel): MessageDialogModel = MessageDialogModel(source)
            override fun newArray(size: Int): Array<MessageDialogModel?> = arrayOfNulls(size)
        }
    }
}
