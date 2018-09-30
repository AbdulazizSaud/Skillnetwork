package com.example.boxtech.skillnetwork.Cores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.boxtech.skillnetwork.CoresAbstract.Chat;
import com.example.boxtech.skillnetwork.Fragments.ChatMessage;
import com.example.boxtech.skillnetwork.Services.CallbackHandlerCondition;
import com.example.boxtech.skillnetwork.Services.HandlerCondition;
import com.example.boxtech.skillnetwork.untill.setMessagePack;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FB_PRIVATE_CHAT_PATH;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_CHAT_USERS_LIST_PATH;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_COUNTER_PATH;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_PRIVATE_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_USER_PRIVATE_CHAT;

public class ChatCore extends Chat {

    private String chatRoomKey = null;
    private String roomName = null, roomPictureUrl = null, chatRoomType = null;
    private long lastMessageCounter;
    private DatabaseReference refRoom, refMessages;
    private String opponentId;
    private boolean isPrivate;
    private String currentUID;

    private SharedPreferences mPrefs;



    private ChildEventListener messagesPacketsListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            addMessage(dataSnapshot);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            addMessage(dataSnapshot);

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener counterListener = new ValueEventListener() {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() == null)
                return;

            final String UID = app.getUserInformation().getUID();
            lastMessageCounter = Long.valueOf(dataSnapshot.getValue().toString().trim());

            refRoom.child(FIREBASE_COUNTER_PATH).setValue(lastMessageCounter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    public void setupChat() {


        //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        chatRoomType = i.getStringExtra("room_type");
        roomName = i.getStringExtra("room_name");
        roomPictureUrl = i.getStringExtra("room_picture");

        // Set the oponent name to use it in add friend method
        opponentName = roomName ;

        isPrivate = chatRoomType.equals(FIREBASE_PRIVATE_ATTR);
        currentUID = app.getUserInformation().getUID();

        opponentId = i.getStringExtra("opponent_key");
        mPrefs = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        refRoom = app.getDatabaseChatPrivate().child(chatRoomKey);
        refMessages = refRoom.child("_messages_");

        // here will be to procedure  in two condition : private, public
        // in case private : it will check the type than it will escape the current user till it find a bio of oppsite user and added to bio of the chat
        // in case public : it will check the type than it will add all user in chat in bio of the chat

        int cooldownTime = 0;


        new HandlerCondition(new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                loadMessages();
                return true;
            }
        },cooldownTime);


        setRoomDetails(roomName, roomPictureUrl);

    }




    public void loadMessages() {

        refMessages.child("_last_message_/counter").addValueEventListener(counterListener);
        refMessages.child("_packets_").addChildEventListener(messagesPacketsListener);
    }


    private void addMessage(DataSnapshot dataSnapshot) {


        if (isEmpty(dataSnapshot))
            return;


        ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

        String senderId = message.getUsername();

        boolean isYou = senderId.equals(app.getUserInformation().getUID());
        message.setMe(isYou);

        message.setUsername(roomName);
        addMessage(message);

    }

    @Override
    protected void sendMessageToFirebase(String messageText) {

        if (sendMessage(messageText)) {
            new setMessagePack(refMessages, messageText, app.getUserInformation().getUID(), ++lastMessageCounter);
        }
    }


    private boolean isEmpty(DataSnapshot dataSnapshot) {
        return dataSnapshot.child("username").getValue() == null || dataSnapshot.child("message").getValue() == null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        refMessages.child("_packets_").removeEventListener(messagesPacketsListener);
        refMessages.child("_last_message_/_counter_").removeEventListener(counterListener);

    }


}
