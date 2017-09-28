package com.fitlinks.message.android.Message.Model

/**
 * Created by venu on 22-09-2017.
 */

import com.fitlinks.message.android.Message.Models.InvitieResponse
import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IUser
import java.util.ArrayList

/**
 * Created by venu on 22-09-2017.
 */

class ChatModels : IDialog<MessageModel> {
    lateinit var messageDialogModel: MessageDialogModel
    var count: Int = 0 //count of unread messasges
    var message: MessageModel? = null

    fun setChatLastMessage(msg: MessageModel) {
        message = msg
    }

    //return channel id
    override fun getId(): String {
        return messageDialogModel.channelId
    }

    //view img of person chatting
    override fun getDialogPhoto(): String {
        return ""
    }

    //view name of person chatting
    override fun getDialogName(): String {
       return ""
    }


    //return arraylist of email,img,name ---- return user details
    override fun getUsers(): List<IUser> {
        val userList = ArrayList<InvitieResponse>()
        val usr = InvitieResponse()
        if (!messageDialogModel.currentUserCoach) {
            usr.invitie_email = messageDialogModel.coachEmail
            usr.invitie_img = messageDialogModel.coachImg
            usr.invitie_name = messageDialogModel.coachName
            userList.add(usr)
        } else {
            usr.invitie_email = messageDialogModel.playerEmail
            usr.invitie_img = messageDialogModel.playerImg
            usr.invitie_name = messageDialogModel.playerName
            userList.add(usr)
        }
        return userList
    }

    //return the msg
    override fun getLastMessage(): MessageModel? {
        if (message != null)
            return message
        else
            return MessageModel()
    }

    //set the last msg
    override fun setLastMessage(msg: MessageModel) {
        message = msg
    }

    //count of msgs unread returned
    override fun getUnreadCount(): Int {
        return count
    }

    
}
