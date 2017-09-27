package com.fitlinks.message.android;

import android.view.View;

import com.fitlinks.message.android.model.MessageModel;
import com.stfalcon.chatkit.messages.MessageHolders;

public class CustomOutcomingTextMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<MessageModel> {

    public CustomOutcomingTextMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(MessageModel message) {
        super.onBind(message);

    }
}
