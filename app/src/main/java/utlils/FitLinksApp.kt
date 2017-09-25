package com.athleteofthemind

import android.app.Application
import android.content.Context
import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.UploadTask
import com.fitlinks.message.android.model.MessageDialogModel
import com.fitlinks.message.android.model.MessageModel
/*import utils.FitLinksPref*/
import com.fitlinks.message.android.model.ChatModel


class FitLinksApp : Application() {



    var userObject: MessageModel? = null

    var isCoach = false

    var currentDialog: ChatModel? = null
    //var preferences = FitLinksPref()
    companion object {
        lateinit var sInstance: FitLinksApp
            private set
    }

    fun getInstance(): FitLinksApp {
        return sInstance
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

    }
}
