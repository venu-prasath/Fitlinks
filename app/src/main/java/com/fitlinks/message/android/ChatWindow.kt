package com.fitlinks.message.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import com.athleteofthemind.FitLinksApp
import com.fitlinks.message.android.model.ChatModel
import com.fitlinks.message.android.model.MessageDialogModel
import com.fitlinks.message.android.model.MessageModel
import com.fitlinks.GlideImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.activity_chat_window.*
import model.InvitieResponse
import utils.FirebaseHandler
import utils.FitLinksPref
import utils.hide
import utils.show
import java.util.*


/**
 * Created by nasima on 24/09/17.
 */
class ChatWindow: AppCompatActivity() , MessageInput.InputListener, MessageInput.AttachmentsListener{

    protected var senderId = ""
    private var channelId = ""
    lateinit var imageLoader: ImageLoader
    lateinit var messagesAdapter: MessagesListAdapter<MessageModel>
    lateinit var chatMessagesList: ArrayList<MessageModel>
    lateinit var otherUser: InvitieResponse
    lateinit var dialogModel: MessageDialogModel
    var preferences = FitLinksPref()


    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


    var chatDialog: ChatModel? = null

    companion object {
        fun newIntent(context: Context, dialog: ChatModel, otherUser: InvitieResponse): Intent {
            val intent = Intent(context, ChatWindow::class.java)
            intent.putExtra("channelId", dialog.messageDialogModel.channelId)
            intent.putExtra("otherUser", otherUser)
            intent.putExtra("dialogModel", dialog.messageDialogModel)
            return intent
        }
    }




    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_chat_window)



        imageLoader = GlideImageLoader()
        chatMessagesList = ArrayList()
        channelId = intent.extras["channelId"] as String
        otherUser = intent.extras["otherUser"] as InvitieResponse
        dialogModel = intent.extras["dialogModel"] as MessageDialogModel
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = otherUser.invitie_name

        bottomSheetBehavior = BottomSheetBehavior.from(cardAttachmentBottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    scrim.hide()
                    input.inputEditText.isEnabled = true
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    scrim.show()
                    input.inputEditText.isEnabled = false
                }
            }
        })

        messagesAdapter = MessagesListAdapter(senderId, /* getMessageHolder(),*/ imageLoader)
        messagesList.setAdapter(messagesAdapter)
        messagesAdapter.addToEnd(chatMessagesList, true)




    }

    override fun onSubmit(input: CharSequence?): Boolean {
        val msg = MessageModel(msgId = "msg_" + chatMessagesList.size + 1,
                receiverId = otherUser.invitie_email, receiverName = otherUser.invitie_name, receiverImg = otherUser.invitie_img,
                //ownerImg = FitLinksApp.sInstance.userObject!!., ownerName =FitLinksApp.sInstance.userObject!!.user_fullname,
                //ownerId = FitLinksApp.sInstance.userObject!!.email,
                message = input.toString(), channelId = channelId)
        FirebaseHandler.getMessages().child(channelId).push().setValue(msg)
        var user = FirebaseAuth.getInstance().currentUser
        val emailKey = (user!!.email)!!.replace("[-+.^:,]".toRegex(), "_dot_")
        val otherUserEmailKey = (otherUser.invitie_email).replace("[-+.^:,]".toRegex(), "_dot_")
        val postValues = HashMap<String, Any>()
        postValues.put("last_chat", input.toString())
        postValues.put("last_user", ""/*.sInstance.userObject!!.email)*/)
        postValues.put("updatedTimeStamp", Date().time)
        FirebaseHandler.getMessageDialogs().child(emailKey).child(otherUserEmailKey).updateChildren(postValues)
        FirebaseHandler.getMessageDialogs().child(otherUserEmailKey).child(emailKey).updateChildren(postValues)
        return true
    }

    override fun onAddAttachments() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


}



