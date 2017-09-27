import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

import com.fitlinks.message.android.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class ChatFragment : BaseFragment() {
    private var sendBtn: Button? = null
    private var chatterText: TextView? = null
    private var logoutBtn: Button? = null
    private var messageTxt: EditText? = null
    private var messagesList: RecyclerView? = null
    private var adapter: ChatMessageAdapter? = null
    private var imageBtn: ImageButton? = null
    private var app: FirebaseApp? = null
    private var database: FirebaseDatabase? = null
    private var storage: FirebaseStorage? = null
    private var databaseRef: DatabaseReference? = null
    private var storageRef: StorageReference? = null
    private var username: String? = null
    private var callback: Listener? = null


    fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View {
        val mainView = inflater.inflate(R.layout.fragment_chat, container, false)
        username = PrefsHelper.getUserName()
        bindView(mainView)
        initView()
        initFirebase()
        return mainView
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(getActivity())
        messagesList!!.setHasFixedSize(false)
        messagesList!!.layoutManager = layoutManager

        // Show an image picker when the user wants to upload an imasge
        imageBtn!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, getString(R.string.complete_action_with)), RC_PHOTO_PICKER)
        }

        sendBtn!!.setOnClickListener {
            val chat = ChatMessage(messageTxt!!.text.toString(), username)
            // Push the chat message to the database
            databaseRef!!.push().setValue(chat)
            messageTxt!!.setText("")
        }
        adapter = ChatMessageAdapter(getActivity())
        messagesList!!.adapter = adapter
        // When record added, list will scroll to bottom
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                messagesList!!.smoothScrollToPosition(adapter!!.getItemCount())
            }
        })
        logoutBtn!!.setOnClickListener { callback!!.onLogoutClicked() }
        chatterText!!.setText(String.format(getString(R.string.chatting_as_s), PrefsHelper.getUserName()))
    }

    private fun initFirebase() {
        // Get the Firebase app and all primitives we'll use
        app = FirebaseApp.getInstance()
        database = FirebaseDatabase.getInstance(app!!)
        storage = FirebaseStorage.getInstance(app!!)

        // Get a reference to our chat "room" in the database
        databaseRef = database!!.getReference("chat_" + getString(R.string.app_name))

        // Listen for when child nodes get added to the collection
        databaseRef!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, s: String) {
                // Get the chat message from the snapshot and add it to the UI
                val chat = snapshot.getValue<ChatMessage>(ChatMessage::class.java)
                adapter!!.addMessage(chat)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(this.toString() + " must implement ChatFragment.Listener")
        }

    }

    private fun bindView(container: View) {
        chatterText = container.findViewById(R.id.chatting_as_text) as TextView
        logoutBtn = container.findViewById(R.id.logoutBtn) as Button
        sendBtn = container.findViewById(R.id.sendBtn) as Button
        messageTxt = container.findViewById(R.id.messageTxt) as EditText
        messagesList = container.findViewById<View>(R.id.messagesList) as RecyclerView
        imageBtn = container.findViewById(R.id.imageBtn) as ImageButton
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //Recieved result from image picker
        if (requestCode == RC_PHOTO_PICKER && resultCode == getActivity().RESULT_OK) {
            val selectedImageUri = data.data
            // Get a reference to the location where we'll store our photos
            storageRef = storage!!.getReference("chat_photos" + getString(R.string.app_name))
            // Get a reference to store file at chat_photos/<FILENAME>
            val photoRef = storageRef!!.child(selectedImageUri.lastPathSegment)
            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(getActivity(), OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        // When the image has successfully uploaded, we get its download URL
                        val downloadUrl = taskSnapshot.downloadUrl
                        // Send message with Image URL
                        val chat = ChatMessage(downloadUrl!!.toString(), username)
                        databaseRef!!.push().setValue(chat)
                        messageTxt!!.setText("")
                    })
        }
    }

    interface Listener {
        fun onLogoutClicked()

    }

    companion object {

        private val RC_PHOTO_PICKER = 1

        val TAG = "ChatFragment"

        fun newInstance(): ChatFragment {
            val args = Bundle()
            val fragment = ChatFragment()
            fragment.setArguments(args)
            return fragment
        }
    }
}