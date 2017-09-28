package com.fitlinks.message.android.Message

/**
 * Created by venu on 22-09-2017.
 */
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fitlinks.message.android.Message.Model.ChatModels
import com.fitlinks.message.android.Message.Model.MessageDialogModel
import com.fitlinks.message.android.Message.Model.MessageModel
import com.fitlinks.message.android.R
import com.google.firebase.database.*
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import kotlinx.android.synthetic.main.fragment_message_list.*



class MessageDialogListFragment: Fragment(), DialogsListAdapter.OnDialogClickListener<ChatModels>,
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
        return inflater!!.inflate(R.layout.fragment_message_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogs = ArrayList()
        dialogListAdapter = DialogsListAdapter(R.layout.item_message_dialoglist, GlideImageLoader())
        dialogListAdapter.setOnDialogClickListener(this)
        messageList.setAdapter(dialogListAdapter)
        Log.i("TEST1","ABOVE FETCHMESSAGEDIALOG")
        fetchMessageDialogs()
        dialogListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                Log.i("DATA OBSERVER","INSIDE DATA OBS1")
                super.onItemRangeRemoved(positionStart, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                Log.i("DATA OBSERVER1","INSIDE DATA OBS2")
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                Log.i("DATA OBSERVER1","INSIDE DATA OBS3")
                super.onItemRangeInserted(positionStart, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                Log.i("DATA OBSERVER1","INSIDE DATA OBS4")
                super.onItemRangeChanged(positionStart, itemCount)
                checkAdapterIsEmpty()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                Log.i("DATA OBSERVER1","INSIDE DATA OBS5")
                super.onItemRangeChanged(positionStart, itemCount, payload)
                checkAdapterIsEmpty()
            }

            override fun onChanged() {
                Log.i("DATA OBSERVER1","INSIDE DATA OBS6")
                super.onChanged()
                checkAdapterIsEmpty()
            }
        })
        checkAdapterIsEmpty()
    }

    private fun checkAdapterIsEmpty() {
        if (activity != null)
            if (dialogListAdapter.itemCount == 0) {
                Log.i("check adapter","INSIDE checkadapter")
                tvEmptyResults.visibility = View.VISIBLE
                tvEmptyResults.post({
                    /*   if (!currentQuery.isBlank()) {
                           tvEmptyResults.text = getString(R.string.no_results_found)
                       } else {*/
                    if (activity is MessageHolderActivity) {
                        Log.i("DATA OBSERVER","INSIDE inner IF")
                        tvEmptyResults.text = resources.getString(R.string.no_chat_dialogs_coach)
                    } else { //Player
                        Log.i("DATA OBSERVER","INSIDE inner ELSE")
                        tvEmptyResults.text = resources.getString(R.string.no_chat_dialogs_player)
                    }
                    //  }
                })
            } else {
                tvEmptyResults.visibility = View.GONE
            }
    }

    fun getMessageDialogs(): DatabaseReference {
        Log.i("FUN GETMESSGEDIALOGS","INSIDE getMessageDialogs")
        val mDatabaseRef = FirebaseDatabase.getInstance().reference
        return mDatabaseRef.child("Message_Dialogs")
    }

    private fun fetchMessageDialogs() {
        //   val progressDialog = activity.showProgress("Loading messages")
        var user = "nasima_dot_hakkim@cognitiveclouds.com" //FirebaseAuth.getInstance().currentUser
        Log.i("TEST2","INSIDE FETCH, CURRENT USER RECEIVED"+user)
        val emailKey = ("nasima.hakkim@cognitiveclouds.com")!!.replace("[-+.^:,]".toRegex(), "_dot_")
        Log.i("TEST3","EMAIL KEY IS RECEIVED")
        getMessageDialogs().child(emailKey).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot?, p1: String?) {
                Log.i("TEST4","INSIDE LISTENER ONCHILDCHANGED")
                val dialogModel = snapshot!!.getValue(MessageDialogModel::class.java)
                val pos = dialogs.indexOfFirst { it.messageDialogModel.channelId == dialogModel!!.channelId }
                if (pos != -1) {
                    val chatModel = dialogs[pos]
                    chatModel.messageDialogModel = dialogModel!!
                    if (!dialogModel.last_chat.isEmpty()) {
                        Log.i("last_chat is Empty","INSIDE IF CONDITION")
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
                Log.i("LAST_CHAT",""+dialogModel.last_chat)
                //AOTMApp.sInstance.messageChannelNotificationMap.put(dialogModel.channelId, true)
                if (!dialogModel.last_chat.isEmpty()) { //why are we checking?
                    Log.i("ON CHILD ADDED","INSIDE IF")
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


    fun onBackClick(): Boolean {
        return false
    }

    override fun onDialogClick(dialog: ChatModels) {

    }

    override fun onResume() {
        super.onResume()
        dialogListAdapter.notifyDataSetChanged()
        Log.i("TEST5","INSIDE ONRESUME")
    }


    override fun onDialogLongClick(dialog: ChatModels?) {
    }

}

