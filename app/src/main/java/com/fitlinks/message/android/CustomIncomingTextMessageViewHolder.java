package com.fitlinks.message.android;

import android.view.View;
import android.widget.TextView;

import com.fitlinks.message.android.model.MessageModel;
import com.stfalcon.chatkit.messages.MessageHolders;

public class CustomIncomingTextMessageViewHolder
        extends MessageHolders.BaseIncomingMessageViewHolder<MessageModel> {

    private TextView messageText;


    public CustomIncomingTextMessageViewHolder(View itemView) {
        super(itemView);
        //Timber.tag("messageHolder").d(itemView.getClass().getSimpleName() + "");
        messageText = itemView.findViewById(R.id.messageText);
    }

    @Override
    public void onBind(MessageModel message) {
        super.onBind(message);
        messageText.setText(message.getText());

    }

}
