package com.fitlinks.message.android.Message

import android.support.v4.app.Fragment

/**
 * Created by venu on 25-09-2017.
 */
class MessageDialogListActvity: SingleFragmentActivity() {
    override protected fun createFragment(): Fragment {
        return MessageDialogListFragment()
    }
}