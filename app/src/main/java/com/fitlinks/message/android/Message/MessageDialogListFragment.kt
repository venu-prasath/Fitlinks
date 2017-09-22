package com.fitlinks.message.android.Message

/**
 * Created by venu on 22-09-2017.
 */
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fitlinks.message.android.Message.Model.ChatModels
import com.fitlinks.message.android.R


/**
 * Created by venu on 22-09-2017.
 */
class MessageDialogListFragment: Fragment() {
    lateinit var dialogs: ArrayList<ChatModels>
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MessageListAdapter? = null
    private var mImageView: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_message_dialoglist, container, false)
    }

    override fun onViewCreated(view: View?,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        dialogs
    }


    class Holder: RecyclerView.ViewHolder() {

    }

    class MessageListAdapter: RecyclerView.Adapter<Holder> {

    }

}