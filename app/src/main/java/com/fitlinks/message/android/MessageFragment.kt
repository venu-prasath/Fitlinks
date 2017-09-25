package com.fitlinks.message.android

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.athleteofthemind.FitLinksApp
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.stfalcon.chatkit.messages.MessageInput
import kotlinx.android.synthetic.main.fragment_message.*
import com.fitlinks.GlideImageLoader
import com.fitlinks.message.android.model.ChatModel
import com.fitlinks.message.android.TrainerDashboardActivity
import com.fitlinks.message.android.model.MessageDialogModel
import com.fitlinks.message.android.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import model.InvitieResponse
import utils.*
//import com.github.ajalt.timberkt.Timber


/**
 * Created by nasima on 24/09/17.
 */
class MessageFragment: Fragment(), OnBackClickListener ,DialogsListAdapter.OnDialogClickListener<ChatModel>,
        DialogsListAdapter.OnDialogLongClickListener<ChatModel> {

    lateinit var dialogs: ArrayList<ChatModel>
    lateinit var dialogListAdapter: DialogsListAdapter<ChatModel>
    val preferences: FitLinksPref = FitLinksPref()
    var currentChatDialog: ChatModel? = null

    companion object {
        /**
         * new instance pattern for fragment
         */
        @JvmStatic
        fun newInstance(): MessageFragment {
            val fragment = MessageFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    private var backButtonHandler: BackButtonHandlerInterface? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        backButtonHandler = context as BackButtonHandlerInterface?
        backButtonHandler!!.addBackClickListener(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }



    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogs = ArrayList()
        dialogListAdapter.setOnDialogClickListener(this)
        messageList.setAdapter(dialogListAdapter)
        if (activity != null)
            if (activity is TrainerDashboardActivity) {
                emptyResults.text = resources.getString(R.string.no_chat_dialogs_trainer)
            } else {
                emptyResults.text = resources.getString(R.string.no_chat_dialogs_buddy)
            }
        fetchMessageDialogs()
        dialogListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                checkAdapterIsEmpty()
            }

            override fun onChanged() {
                super.onChanged()
                checkAdapterIsEmpty()
            }
        })
        checkAdapterIsEmpty()
    }

    private fun checkAdapterIsEmpty() {
        if (activity != null)
            if (dialogListAdapter.itemCount == 0) {
                emptyResults.show()
                emptyResults.post({
                    if (activity is TrainerDashboardActivity) {
                        emptyResults.text = resources.getString(R.string.no_chat_dialogs_trainer)
                    } else {
                        emptyResults.text = resources.getString(R.string.no_chat_dialogs_buddy)
                    }
                })
            } else {
                emptyResults.hide()
            }

    }

    private fun fetchMessageDialogs() {
        //   val progressDialog = activity.showProgress("Loading messages")
        var user = FirebaseAuth.getInstance().currentUser
        val emailKey = (user!!.email)!!.replace("[-+.^:,]".toRegex(), "_dot_")
        FirebaseHandler.getMessageDialogs().child(emailKey).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot?, p1: String?) {
                val dialogModel = snapshot!!.getValue(MessageDialogModel::class.java)
                val pos = dialogs.indexOfFirst { it.messageDialogModel.channelId == dialogModel!!.channelId }
                if (pos != -1) {
                    val chatModel = dialogs[pos]
                    chatModel.messageDialogModel = dialogModel!!
                    if (!dialogModel.last_chat.isEmpty()) {
                        val msgModel = MessageModel(msgId = "msg_last",
                                ownerImg = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyImg else dialogModel.trainerImg,
                                receiverId = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyEmail else dialogModel.trainerEmail,
                                ownerName = dialogModel.last_user,
                                receiverName = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyName else dialogModel.trainerName,
                                updatedTimeStamp = dialogModel.updatedTimeStamp,
                                receiverImg = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyImg else dialogModel.trainerImg,
                                message = dialogModel.last_chat,
                                ownerId = "",
                                channelId = dialogModel.channelId)
                        chatModel.setChatLastMessage(msgModel)
                        chatModel.count = chatModel.count + 1
                    }
                    dialogs.removeAt(pos)
                    dialogs.add(pos, chatModel)
                    dialogListAdapter.updateItem(pos, chatModel)
                }

            }

            override fun onChildAdded(snapshot: DataSnapshot?, p1: String?) {
                //Timber.tag("bug_here").d("called ")
                val dialogModel = snapshot!!.getValue(MessageDialogModel::class.java)
                dialogModel!!.currentUserTrainer = preferences.isTrainerUser
                val chatDialog = ChatModel()
                if (!dialogModel.last_chat.isEmpty()) {
                    val msgModel = MessageModel(msgId = "msg_last",
                            ownerImg = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyImg else dialogModel.trainerImg,
                            receiverId = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyEmail else dialogModel.trainerEmail,
                            ownerName = dialogModel.last_user,
                            receiverName = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyName else dialogModel.trainerName,
                            updatedTimeStamp = dialogModel.updatedTimeStamp,
                            receiverImg = if (dialogModel.last_user == dialogModel.buddyEmail) dialogModel.buddyImg else dialogModel.trainerImg,
                            message = dialogModel.last_chat,
                            ownerId = "",
                            channelId = dialogModel.channelId)
                    chatDialog.setChatLastMessage(msgModel)
                }
                chatDialog.messageDialogModel = dialogModel
                dialogs.add(chatDialog)
                dialogListAdapter.addItem(chatDialog)
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
                val dialogModel = snapshot!!.getValue(MessageDialogModel::class.java)
                val pos = dialogs.indexOfFirst { it.messageDialogModel.channelId == dialogModel!!.channelId }
                if (pos != -1) {
                    dialogs.removeAt(pos)
                    dialogListAdapter.notifyDataSetChanged()
                }
            }
        })

    }


    override fun onDialogClick(dialog: ChatModel) {
        var otherUser: InvitieResponse
        dialog.count = 0
        if (FitLinksApp.sInstance.isCoach) {
            otherUser = InvitieResponse(invitie_email = dialog.messageDialogModel.buddyEmail, invitie_img = dialog.messageDialogModel.buddyImg, invitie_name = dialog.messageDialogModel.buddyName)
        } else {
            otherUser = InvitieResponse(invitie_email = dialog.messageDialogModel.trainerEmail, invitie_img = dialog.messageDialogModel.trainerImg, invitie_name = dialog.messageDialogModel.trainerName)
        }
        FitLinksApp.sInstance.currentDialog = dialog
        startActivity(ChatWindow.newIntent(activity, dialog, otherUser))
    }


    override fun onDialogLongClick(dialog: ChatModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackClick(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

