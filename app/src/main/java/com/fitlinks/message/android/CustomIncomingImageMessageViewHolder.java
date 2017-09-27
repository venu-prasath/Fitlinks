package com.fitlinks.message.android;

import android.view.View;
import com.fitlinks.message.android.model.MessageModel;
import com.stfalcon.chatkit.messages.MessageHolders;


public class CustomIncomingImageMessageViewHolder
        extends MessageHolders.IncomingImageMessageViewHolder<MessageModel> {


    public CustomIncomingImageMessageViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void onBind(MessageModel message) {
        super.onBind(message);
    }
}
