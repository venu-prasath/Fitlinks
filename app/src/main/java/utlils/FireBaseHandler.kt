package utils

import android.net.Uri
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * Created by nasima on 23/09/17.
 */


class FirebaseHandler {
    companion object {

        fun getFirebaseAuth(): FirebaseAuth {
            val mAuth = FirebaseAuth.getInstance()
            return mAuth
        }

        fun getFirebaseStorage(): StorageReference {
            val mStorageRef = FirebaseStorage.getInstance().reference
            return mStorageRef
        }

        fun getFirebaseDatabase(): DatabaseReference {
            val mDatabaseRef = FirebaseDatabase.getInstance().reference
            return mDatabaseRef
        }


        fun getUserRelations(): DatabaseReference {
            val mDatabaseRef = FirebaseDatabase.getInstance().reference
            return mDatabaseRef.child("user_relations")
        }


        fun getMessageDialogs(): DatabaseReference {
            val mDatabaseRef = FirebaseDatabase.getInstance().reference
            return mDatabaseRef.child("Message_Dialogs")
        }

        fun getMessages(): DatabaseReference {
            val mDatabaseRef = FirebaseDatabase.getInstance().reference
            return mDatabaseRef.child("Messages")
        }

        fun getUserForKey(emailKey: String): DatabaseReference {
            val mDatabaseRef = FirebaseDatabase.getInstance().reference
            return mDatabaseRef.child("users").child(emailKey)
        }


        fun getFirebaseBaseUrl(): String {
            val BASE_URL = "https://atom-f2b69.firebaseio.com"
            return BASE_URL
        }
    }
}