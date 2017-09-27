package com.fitlinks.message.android

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.athleteofthemind.FitLinksApp
import com.fitlinks.message.android.model.ChatModel
import com.fitlinks.message.android.model.MessageDialogModel
import com.fitlinks.message.android.model.MessageModel
import com.fitlinks.GlideImageLoader
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.activity_chat_window.*
import kotlinx.android.synthetic.main.activity_chat_window.view.*
import kotlinx.android.synthetic.main.item_custom_incoming_image_message.*
import kotlinx.android.synthetic.main.item_custom_incoming_image_message.view.*
import kotlinx.android.synthetic.main.item_custom_outcoming_image_message.*
import model.InvitieResponse
import utils.FirebaseHandler
import utils.FitLinksPref
import utils.hide
import utils.show
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



/**
 * Created by nasima on 24/09/17.
 */
class ChatWindow() : AppCompatActivity(),MessageInput.InputListener, MessageInput.AttachmentsListener{


    var tag: String = "key"
    protected var senderId = ""
    private var channelId = ""
    lateinit var imageLoader: ImageLoader
    lateinit var messagesAdapter: MessagesListAdapter<MessageModel>
    lateinit var chatMessagesList: ArrayList<MessageModel>
    lateinit var otherUser: InvitieResponse
    lateinit var dialogModel: MessageDialogModel
    var preferences = FitLinksPref()

    val CAMERA_REQUEST_CODE = 0
    lateinit var imageFilePath: String



    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


    private var CameraButton: ImageButton? = null
    private var GalleryButton: ImageButton? =null
    //private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val RC_PHOTO_PICKER = 8

    private var database: FirebaseDatabase? = null
    private var storage: FirebaseStorage? = null
    private var databaseRef: DatabaseReference? = null
    private var storageRef: StorageReference? = null


    var chatDialog: ChatModel? = null



    companion object {

        private val IMAGE_DIRECTORY = "/demonuts"

        fun newIntent(context: Context, dialog: ChatModel, otherUser: InvitieResponse): Intent {
            val intent = Intent(context, ChatWindow::class.java)
            //intent.putExtra("channelId", dialog.messageDialogModel.channelId)
            intent.putExtra("otherUser", otherUser)
            intent.putExtra("dialogModel", dialog.messageDialogModel)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d(tag, "chat")

        setContentView(R.layout.activity_chat_window)
        setSupportActionBar(toolbar)

        CameraButton= findViewById<View>(R.id.Camera) as ImageButton
        GalleryButton=findViewById<View>(R.id.Gallery) as ImageButton
       // imageview =findViewById<View>(R.id.image) as ImageView

        //CameraButton!!.setOnClickListener { choosePhotoFromGallary() }
        //GalleryButton!!.setOnClickListener { choosePhotoFromCamera() }

        GalleryButton!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, getString(R.string.complete_action_with)), RC_PHOTO_PICKER)
        }

        input.button.setImageDrawable(resources.getDrawable(R.drawable.ic_submit))
        imageLoader = GlideImageLoader()
        chatMessagesList = ArrayList()
        // channelId = intent.extras["channelId"] as String
//        otherUser = intent.extras["otherUser"] as InvitieResponse
        //  dialogModel = intent.extras["dialogModel"] as MessageDialogModel
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Nasima"

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

        messagesAdapter = MessagesListAdapter(senderId, imageLoader)
        messagesList.setAdapter(messagesAdapter)
        messagesAdapter.addToEnd(chatMessagesList, true)

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


        input.setInputListener(this@ChatWindow)
        input.setAttachmentsListener(this@ChatWindow)
        fetchMessagesForChannel(channelId)
        messagesAdapter.setLoadMoreListener { page, totalItemsCount ->
            if (totalItemsCount >= 100)
                FirebaseHandler.getMessages().child(channelId).endAt(oldestKeySeen).limitToLast(100).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        p0!!.toException().toString()
                    }

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        if (snapshot!!.exists()) {
                            val list = ArrayList<MessageModel>()
                            for (child in snapshot.children) {
                                val messageModel = child.getValue(MessageModel::class.java)
                                if (messageModel!!.ownerId == "nasima7878@gmail.com")//email)
                                    messageModel.isOutComing = true
                                list.add(messageModel)
                                messagesAdapter.addToEnd(list, false)
                            }
                        }
                    }
                })
        }

    }

    private var oldestKeySeen: String = ""


    private fun fetchMessagesForChannel(channelId: String) {
        FirebaseHandler.getMessages().child(channelId).limitToLast(100).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }


            override fun onChildAdded(snapshot: DataSnapshot?, p1: String?) {
                if (oldestKeySeen.isEmpty())
                    oldestKeySeen = snapshot!!.key
                val messageModel = snapshot!!.getValue(MessageModel::class.java)
                if (messageModel!!.ownerId == "nasima7878@gmail.com")
                    messageModel.isOutComing = true
                chatMessagesList.add(messageModel)
                messagesAdapter.addToStart(messageModel, true)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        })
    }

    /*fun choosePhotoFromGallary() {
            val galleryIntent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY)
    }

    private fun choosePhotoFromCamera() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    saveImage(bitmap)
                    var messageModel:MessageModel= MessageModel()
                    messageModel.image= contentURI.toString()
                    chatMessagesList.add(messageModel)
                    messagesAdapter.notifyDataSetChanged()
                    uploadImage()

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras.get("data") as Bitmap
            image!!.setImageBitmap(thumbnail)
        }
    }

    private fun uploadImage() {
        FirebaseStorage.getInstance().child()
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return " "
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //Recieved result from image picker
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
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
                        val chat = MessageModel(downloadUrl!!.toString(), ownerId = "" )
                        databaseRef!!.push().setValue(chat)
                        MessageModel.message!!.setText("")
                    })
        }
    }


    override fun onSubmit(input: CharSequence?): Boolean {
        val msg = MessageModel(msgId = "msg_" + chatMessagesList.size + 1,
                /// receiverId = otherUser.invitie_email, receiverName = otherUser.invitie_name, receiverImg = otherUser.invitie_img,
                //ownerImg = otherUser.invitie_img, ownerName = otherUser.invitie_name ,ownerId = otherUser.invitie_email,
                message = input.toString(), channelId = channelId)
        FirebaseHandler.getMessages().child(channelId).push().setValue(msg)
        var user = FirebaseAuth.getInstance().currentUser
        val emailKey = ("nasima7878@gmail.com"/*user!!.email*/)!!.replace("[-+.^:,]".toRegex(), "_dot_")
        val otherUserEmailKey = ("nasima.hakkim@cognitiveclouds.com"/*otherUser.invitie_email*/).replace("[-+.^:,]".toRegex(), "_dot_")
        val postValues = HashMap<String, Any>()
        postValues.put("last_chat", input.toString())
        postValues.put("last_user", emailKey)
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




