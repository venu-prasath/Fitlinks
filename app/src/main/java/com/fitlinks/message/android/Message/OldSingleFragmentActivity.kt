package com.fitlinks.message.android.Message

/**
 * Created by venu on 22-09-2017.
 */
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.fitlinks.message.android.R
import kotlinx.android.synthetic.main.item_message_dialoglist.*
import kotlinx.android.synthetic.main.old_activity_message.*

/**
 * Created by venu on 22-09-2017.
 */

abstract class OldSingleFragmentActivity : AppCompatActivity() {
    protected abstract fun createFragment(): Fragment

    override protected fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_activity_message)
        setSupportActionBar(Toolbar)
        supportActionBar!!.setTitle("Messages")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        val floatingActionBar = findViewById(R.id.FloatingActionBar) as FloatingActionButton


        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if(fragment == null) {
            floatingActionBar.hide()
            tvEmptyResults.visibility = View.GONE
            fragment = createFragment()
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit()
        }
    }
}