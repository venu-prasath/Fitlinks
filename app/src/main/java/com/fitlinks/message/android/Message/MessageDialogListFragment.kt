package com.fitlinks.message.android.Message

/**
 * Created by venu on 22-09-2017.
 */
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fitlinks.message.android.Message.Model.ChatModels
import com.fitlinks.message.android.R
//import kotlinx.android.synthetic.main.item_message_dialoglist.*

class MessageDialogListFragment: Fragment() {
    lateinit var dialogs: ArrayList<ChatModels>
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MessageListAdapter? = null
    private var mImageView: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.old_fragment_recyclerview_dialoglist, container, false)

        //setSupportActionBar(toolbar)
        mRecyclerView = view?.findViewById<View>(R.id.recycler_view) as RecyclerView
        mRecyclerView?.setLayoutManager(LinearLayoutManager(getActivity()))
        updateUI()
        return view
    }

    private fun updateUI() {
        mAdapter = MessageListAdapter()
        mRecyclerView?.setAdapter(mAdapter)
    }

    public class MessageHolder(itemView: View): RecyclerView.ViewHolder(itemView) { //what parameter
        private var mSender: TextView? = null
        private var mMessage: TextView? = null

        fun MessageHolder(layoutInflater: LayoutInflater,parent: ViewGroup?) {
            mSender = itemView.findViewById(R.id.dialogName)
            mMessage = itemView.findViewById(R.id.dialogLastMessage)
        }

        fun bind() {
            //onBindViewHolder action here
        }

    }

    private class MessageListAdapter: RecyclerView.Adapter<MessageHolder>() {
        private var data = null
        fun MessageListAdapter() {
            //initializing data received from firebase
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageHolder {
            var listView: View = LayoutInflater.from(parent?.getContext()).inflate(R.layout.item_message_dialoglist,parent,false)
            return MessageHolder(listView)
        }

        override fun onBindViewHolder(holder: MessageHolder?, position: Int) {
            holder?.bind()
        }

        override fun getItemCount(): Int {
            return 100
        }
    }

}