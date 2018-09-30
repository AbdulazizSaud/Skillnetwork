package com.example.boxtech.skillnetwork.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Fragments.ChatMessage;
import com.example.boxtech.skillnetwork.R;

import java.util.List;

/**
 * Created by Kay on 10/30/2016.
 */

public class ChatAdapter extends BaseAdapter {

    private final List<ChatMessage> chatMessages;
    private Activity context;



    public ChatAdapter(Activity context, List<ChatMessage> chatMessages)
    {
        this.context=context;
        this.chatMessages = chatMessages;

    }


    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean myMsg = chatMessage.isMe() ;//Just a dummy check to simulate whether it me or other sender
        setAlignment(holder, myMsg,chatMessage.isHotKeys());
        holder.txtMessage.setText(chatMessage.getMessage());
        try {
            holder.txtInfo.setText(App.getInstance().convertFromTimeStampToDate((long)chatMessage.getTimestamp()));

        }catch (ClassCastException e)
        {

        }
        holder.senderUsername.setText(chatMessage.getUsername());

        return convertView;
    }

    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignment(ViewHolder holder, boolean isMe, boolean isHotkey) {

        if(isHotkey)
        {

            holder.contentWithBG.setBackgroundResource(R.drawable.user_left_chat_bg);
            holder.senderUsername.setVisibility(View.GONE);

            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.text_color));
            holder.txtMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            holder.txtInfo.setLayoutParams(layoutParams);

            return;
        }

        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);
            holder.senderUsername.setVisibility(View.VISIBLE);

            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.text_color));
            holder.txtMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
            holder.senderUsername.setVisibility(View.GONE);
            // posioning fucking X and y for the text message -_-
            // holder.txtMessage.setX((holder.contentWithBG.getX())/2);
            //  holder.txtMessage.setY((holder.contentWithBG.getY())/2);
            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.white));
            holder.txtMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_done_all_delivered_16dp,0);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);

        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        Typeface playbold = Typeface.createFromAsset(context.getAssets(), "playbold.ttf");
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.senderUsername = (TextView) v.findViewById(R.id.sender_username_chat_bubble);
        holder.senderUsername.setTypeface(playbold);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }


    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public TextView senderUsername;
    }
}
