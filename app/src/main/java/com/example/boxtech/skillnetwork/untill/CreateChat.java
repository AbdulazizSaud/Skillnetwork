package com.example.boxtech.skillnetwork.untill;

import android.content.Context;
import android.content.Intent;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.ChatCore;
import com.example.boxtech.skillnetwork.Fragments.ChatMessage;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class CreateChat implements FirebasePaths {

    private String chatId;

    private App app;

    public CreateChat()
    {
        app= App.getInstance();
    }
    public CreateChat(String chatId){
        app= App.getInstance();
        this.chatId = chatId;
    }

    private String createPrivateFirebaseChat(String UID, String freelancerId,String requestId) {

        chatId = "";

        // path --> /Chat/_private_
        DatabaseReference refPrivate = app.getDatabaseChatPrivate();
        String key = chatId.isEmpty()?refPrivate.push().getKey():chatId;

        // path --> /Chat/_private/[KEY]
        DatabaseReference chatRoom = refPrivate.child(key);

        // path --> /Chat/_private/[KEY]/_info_
        DatabaseReference roomInfo = chatRoom.child(FIREBASE_DETAILS_ATTR);

        // path --> /Chat/_private/[KEY]/_info_/_users_
        DatabaseReference roomUsers = roomInfo.child(FIREBASE_USERS_LIST_ATTR);

        roomUsers.child(UID).setValue(Constants.ACCOUNT_TYPE_CUSTOMER);
        roomUsers.child(freelancerId).setValue(Constants.ACCOUNT_TYPE_FREELANCER);

        String accessKey = roomInfo.push().getKey();
        roomInfo.child("request_id").setValue(requestId);

        // path --> /Chat/_private/[KEY]/_messages_
        DatabaseReference messagesRef = chatRoom.child(FIREBASE_CHAT_MESSAGES);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(key);
        chatMessage.setMessage("hello");
        chatMessage.setUsername("");
        chatMessage.setTimestamp(ServerValue.TIMESTAMP);
        chatMessage.setCounter(0);

        messagesRef.child("_last_message_").setValue(chatMessage);



        // Set Referance for users

        // path --> /_users_info_/[UID]/_chat_refs_/_private_

        String privateChatPath = UID + "/" + FIREBASE_USER_PRIVATE_CHAT;
        DatabaseReference refUsePrivaterChats = app.getDatabaseUsersInfo().child(privateChatPath);
        refUsePrivaterChats.child(key).child(FIREBASE_COUNTER_PATH).setValue(0);
        refUsePrivaterChats.child(key).child(FIREBASE_OPPONENT_ID_ATTR).setValue(freelancerId);


        // Pending


        // path --> /Chat/_pending_chat_/[FRIEND_KEY]/_private_/[UID]
        DatabaseReference refPendingChat =  app.getDatabaseChatPending().child(freelancerId).child(FIREBASE_PRIVATE_ATTR).child(UID);
        refPendingChat.child("chat_id").setValue(key);
        return key;
    }

    public void createPrivateChat(final Context activity, String friendUID, final String oppnenetUsername, final String oppnenetPicture, final String requestId) {
        final String opponentKey = friendUID;
        final String currentUserId = app.getUserInformation().getUID();
        String privateChatPath = currentUserId + "/" + FIREBASE_USER_PRIVATE_CHAT;


        DatabaseReference chatRef = app.getDatabaseUsersInfo().child(privateChatPath);
        final Query query = chatRef.orderByChild(FIREBASE_OPPONENT_ID_ATTR).startAt(opponentKey).endAt(opponentKey + "\uf8ff").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomKey;
                if (dataSnapshot.getValue() == null) {
                    roomKey = createPrivateFirebaseChat(currentUserId, opponentKey,requestId);


                    jumpToPrivateChat(activity,opponentKey,oppnenetUsername,oppnenetPicture, roomKey);
                } else {
                    Iterable<DataSnapshot> chatRoom = dataSnapshot.getChildren();
                    for (DataSnapshot data : chatRoom)
                        jumpToPrivateChat(activity,opponentKey,oppnenetUsername,oppnenetPicture, data.getKey());

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    private void jumpToPrivateChat(Context c ,String oppnenetKey, String oppnentUsername,String opnnentPic, String roomKey) {
        if(c == null)
            return;

        Intent chatActivity = new Intent(c, ChatCore.class);
        chatActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chatActivity.putExtra("room_key", roomKey);
        chatActivity.putExtra("room_type", FIREBASE_PRIVATE_ATTR);
        chatActivity.putExtra("room_name", oppnentUsername);
        chatActivity.putExtra("room_picture", opnnentPic);

        chatActivity.putExtra("friend_key", oppnenetKey);

        c.startActivity(chatActivity);
    }



}
