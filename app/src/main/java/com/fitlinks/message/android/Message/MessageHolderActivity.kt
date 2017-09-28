package com.fitlinks.message.android.Message

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.fitlinks.message.android.R
import kotlinx.android.synthetic.main.old_activity_message.*

/**
 * Created by venu on 27-09-2017.
 */
class MessageHolderActivity: AppCompatActivity() {
    override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_holder)
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "Messages"
        //Log.i("TEMP1","supportActionBar().title set successfully")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if(fragment == null) {
            //Log.i("TEMP","inside if") -- works
            tvEmptyResults?.visibility = View.GONE
            fragment = MessageDialogListFragment.newInstance()
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit()
        }
    }
}