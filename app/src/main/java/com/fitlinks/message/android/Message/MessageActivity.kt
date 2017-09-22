package com.fitlinks.message.android.Message

import android.app.Fragment
import android.support.v7.app.AppCompatActivity

class MessageActivity : AppCompatActivity() {
    override protected fun createFragment(): Fragment {
        return MessageDialogListFragment()
    }

}