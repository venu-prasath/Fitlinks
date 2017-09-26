package com.fitlinks.message.android.Message

/**
 * Created by venu on 22-09-2017.
 */
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fitlinks.message.android.Message.Model.ChatModels
import com.fitlinks.message.android.Message.Model.MessageDialogModel
import com.fitlinks.message.android.Message.Model.MessageModel
import com.fitlinks.message.android.Message.Models.InvitieResponse
import com.fitlinks.message.android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import kotlinx.android.synthetic.main.fragment_recyclerview_dialoglist.*



class OldMessageDialogListFragment: Fragment(), DialogsListAdapter.OnDialogClickListener<ChatModels>,
        DialogsListAdapter.OnDialogLongClickListener<ChatModels> {
    lateinit var dialogs: ArrayList<ChatModels>
    lateinit var dialogListAdapter: DialogsListAdapter<ChatModels>
    var currentChatDialog: ChatModels? = null

    companion object {
        /**
         * new instance pattern for fragment
         */
        @JvmStatic
        fun newInstance(): MessageDialogListFragment {
            val fragment = MessageDialogListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    /*
    private var backButtonHandler: BackButtonHandlerInterface? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        backButtonHandler = context as BackButtonHandlerInterface?
        backButtonHandler!!.addBackClickListener(this)
    } */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.activity_message, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogs = ArrayList()
        dialogListAdapter = DialogsListAdapter(R.layout.item_message_dialoglist, GlideImageLoader())
        dialogListAdapter.setOnDialogClickListener(this)
        messageList.setAdapter(dialogListAdapter)
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

    }

    fun getMessageDialogs(): DatabaseReference {
        val mDatabaseRef = FirebaseDatabase.getInstance().reference
        return mDatabaseRef.child("Message_Dialogs")
    }

    private fun fetchMessageDialogs() {
        //   val progressDialog = activity.showProgress("Loading messages")
        var user = FirebaseAuth.getInstance().currentUser
        val emailKey = (user!!.email)!!.replace("[-+.^:,]".toRegex(), "_dot_")
        getMessageDialogs().child(emailKey).addChildEventListener(object : ChildEventListener {
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
                                ownerImg = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerImg else dialogModel.coachImg,
                                receiverId = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerEmail else dialogModel.coachEmail,
                                ownerName = dialogModel.last_user,
                                receiverName = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerName else dialogModel.coachName,
                                updatedTimeStamp = dialogModel.updatedTimeStamp,
                                receiverImg = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerImg else dialogModel.coachImg,
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
                dialogModel!!.currentUserCoach = true
                val chatDialog = ChatModels()
                //AOTMApp.sInstance.messageChannelNotificationMap.put(dialogModel.channelId, true)
                if (!dialogModel.last_chat.isEmpty()) { //why are we checking?
                    val msgModel = MessageModel(msgId = "msg_last",
                            ownerImg = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerImg else dialogModel.coachImg,
                            receiverId = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerEmail else dialogModel.coachEmail,
                            ownerName = dialogModel.last_user,
                            receiverName = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerName else dialogModel.coachName,
                            updatedTimeStamp = dialogModel.updatedTimeStamp,
                            receiverImg = if (dialogModel.last_user == dialogModel.playerEmail) dialogModel.playerImg else dialogModel.coachImg,
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

    /*
    override fun onBackClick(): Boolean {
        return false
    } */

    override fun onDialogClick(dialog: ChatModels) {

    }

    override fun onResume() {
        super.onResume()
        dialogListAdapter.notifyDataSetChanged()
    }


    override fun onDialogLongClick(dialog: ChatModels?) {
    }

}

