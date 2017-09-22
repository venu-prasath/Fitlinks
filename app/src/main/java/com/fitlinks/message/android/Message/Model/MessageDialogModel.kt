package com.fitlinks.message.android.Message.Model

/**
 * Created by venu on 22-09-2017.
 */
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by venu on 22-09-2017.
 */
data class MessageDialogModel(
        var coachName: String = "",
        var playerName: String = "",
        var coachEmail: String = "",
        var coachImg: String = "",
        var playerEmail: String = "",
        var playerImg: String = "",
        var updatedTimeStamp: Long = 0,
        var last_chat: String = "",
        var last_user: String = "",
        var channelId: String = "",
        var currentUserCoach: Boolean = false
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
        writeString(coachName)
        writeString(playerName)
        writeString(coachEmail)
        writeString(coachImg)
        writeString(playerEmail)
        writeString(playerImg)
        writeLong(updatedTimeStamp)
        writeString(last_chat)
        writeString(last_user)
        writeString(channelId)
        writeInt((if (currentUserCoach) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MessageDialogModel> = object : Parcelable.Creator<MessageDialogModel> {
            override fun createFromParcel(source: Parcel): MessageDialogModel = MessageDialogModel(source)
            override fun newArray(size: Int): Array<MessageDialogModel?> = arrayOfNulls(size)
        }
    }
}