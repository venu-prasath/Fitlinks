package com.fitlinks.message.android.model

import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IUser
import java.util.ArrayList
import model.InvitieResponse
import java.util.*

/**
 * Created by Nasi on 22/09/17.
 */


class ChatModel : IDialog<MessageModel> {
    lateinit var messageDialogModel: MessageDialogModel
    var count: Int = 0
    var message: MessageModel? = null

    fun setChatLastMessage(msg: MessageModel) {
        message = msg
    }

    private val isTrainer: Boolean = true

    override fun getId(): String {
        return messageDialogModel.channelId
    }

    override fun getDialogPhoto(): String {
        if (isTrainer)
            return messageDialogModel.buddyImg
        else
            return messageDialogModel.trainerImg
    }

    override fun getDialogName(): String {
        if (isTrainer)
            return messageDialogModel.buddyName
        else
            return messageDialogModel.trainerName
    }

    override fun getUsers(): List<IUser> {
        val userList = ArrayList<InvitieResponse>()
        val usr = InvitieResponse()
        if (!messageDialogModel.currentUserTrainer) {
            usr.invitie_email = messageDialogModel.trainerEmail
            usr.invitie_img = messageDialogModel.trainerImg
            usr.invitie_name = messageDialogModel.trainerName
            userList.add(usr)
        } else {
            usr.invitie_email = messageDialogModel.buddyEmail
            usr.invitie_img = messageDialogModel.buddyImg
            usr.invitie_name = messageDialogModel.buddyName
            userList.add(usr)
        }
        return userList
    }

    override fun getLastMessage(): MessageModel? {
        if (message != null)
            return message
        else
            return MessageModel()
    }

    override fun setLastMessage(msg: MessageModel) {
        message = msg
    }

    override fun getUnreadCount(): Int {
        return count
    }
}
