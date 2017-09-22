package com.fitlinks.message.android.Message.Models

/**
 * Created by venu on 22-09-2017.
 */
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stfalcon.chatkit.commons.models.IUser

data class InvitieResponse(
        @SerializedName("invitie_email")
        var invitie_email: String = "",
        @SerializedName("invitie_img")
        var invitie_img: String = "",
        @SerializedName("invitie_name")
        var invitie_name: String = ""
) : Parcelable, IUser {
    override fun getAvatar(): String {
        return invitie_img
    }

    override fun getName(): String {
        return invitie_name
    }

    override fun getId(): String {
        return invitie_email
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(invitie_email)
        writeString(invitie_img)
        writeString(invitie_name)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<InvitieResponse> = object : Parcelable.Creator<InvitieResponse> {
            override fun createFromParcel(source: Parcel): InvitieResponse = InvitieResponse(source)
            override fun newArray(size: Int): Array<InvitieResponse?> = arrayOfNulls(size)
        }
    }
}
