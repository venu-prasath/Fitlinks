package com.fitlinks.message.android.Message

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fitlinks.message.android.R

/**
* Created by venu on 26-09-2017.
 */
public class MessageListAdapter: RecyclerView.Adapter<MessageDialogListFragment.MessageHolder>() {
    private var data = null
    fun MessageListAdapter() {
        //initializing data received from firebase
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageDialogListFragment.MessageHolder {
        var listView: View = LayoutInflater.from(parent?.getContext()).inflate(R.layout.item_message_dialoglist,parent,false)
        return MessageDialogListFragment.MessageHolder(listView)
    }

    override fun onBindViewHolder(holder: MessageDialogListFragment.MessageHolder?, position: Int) {
        holder?.bind()
    }

    override fun getItemCount(): Int {
        return 100
    }
}
