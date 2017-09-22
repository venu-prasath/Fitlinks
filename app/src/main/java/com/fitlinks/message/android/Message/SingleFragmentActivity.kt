package com.fitlinks.message.android.Message

/**
 * Created by venu on 22-09-2017.
 */
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.venu.fitlinkstest1.R
import com.fitlinks.message.android.R

/**
 * Created by venu on 22-09-2017.
 */

abstract class SingleFragmentActivity: AppCompatActivity() {
    protected abstract fun createFragment(): Fragment

    override protected fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.FragmentContainer)

        if(fragment == null) {
            fragment = createFragment()
            fm.beginTransaction().add(R.id.FragmentContainer,fragment).commit()
        }
    }
}