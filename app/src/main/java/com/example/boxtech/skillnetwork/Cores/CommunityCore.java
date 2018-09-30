package com.example.boxtech.skillnetwork.Cores;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.boxtech.skillnetwork.Fragments.ChatMessage;
import com.example.boxtech.skillnetwork.Fragments.CommunityFragment;
import com.example.boxtech.skillnetwork.Models.CommunityChatModel;
import com.example.boxtech.skillnetwork.Models.Customer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_COUNTER_PATH;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_DETAILS_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_OPPONENT_ID_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_PENDING_CHAT_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_PRIVATE_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_USERNAME_PATH;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_USER_CHAT_REFERENCES;

public class CommunityCore extends CommunityFragment{


    private DatabaseReference refAuthCurrentUserChats;
    private DatabaseReference refUserInfo;
    private DatabaseReference refPendingChat;
    private ChildEventListener privateSingleChatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
            addNewPrivateChat(dataSnapshot);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // each time it's added a new thing
            addNewPrivateChat(dataSnapshot);

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            removeFromList(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ChildEventListener privatePendingChatListener;



    private ValueEventListener lastMessageListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot lastMsgInfo) {

            ChatMessage message = lastMsgInfo.getValue(ChatMessage.class);

            if (message == null)
                return;


            final String chatKey = lastMsgInfo.getRef().getParent().getParent().getKey();
            final String usernameKey = message.getUsername();
            final String msg = message.getMessage();
            final long timeStamp = (long)message.getTimestamp();
            final String currentChatCounterAsString = String.valueOf(message.getCounter());


            app.getDatabaseUsersInfo().child(usernameKey).child(FIREBASE_USERNAME_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String joinerUsername = dataSnapshot.getValue(String.class);
                    if (app.getUserInformation().getUID().equals(usernameKey))
                        joinerUsername = "You";

                    updateLastMessage(chatKey, joinerUsername, msg, timeStamp);
                    setCounter(chatKey,FIREBASE_PRIVATE_ATTR,joinerUsername, msg, currentChatCounterAsString);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private void setCounter(final String chatKey, String chatType, final String joinerUsername, final String msg, final String currentChatCounterAsString) {
        // this listner for chat counter + notify shit

        refAuthCurrentUserChats.child(chatType + "/" + chatKey + "/" + FIREBASE_COUNTER_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userChatRef) {

                if (userChatRef.getValue() == null)
                    return;

                long currentCounter = Long.parseLong(currentChatCounterAsString);
                long value = Long.parseLong(userChatRef.getValue().toString().trim());
                long res = currentCounter - value;

                updateCounter(chatKey, String.valueOf(res));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void addNewPrivateChat(final DataSnapshot chatRef) {
        final String opponentKey = chatRef.child(FIREBASE_OPPONENT_ID_ATTR).getValue(String.class);

        if (opponentKey == null)
            return;

        // on first time
        final DatabaseReference refCurrentChat = app.getDatabaseChat().child(FIREBASE_PRIVATE_ATTR).child(chatRef.getKey());

        refUserInfo = app.getDatabaseUsersInfo().child(opponentKey).child(FIREBASE_DETAILS_ATTR);

        refUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot userInfo) {


                refCurrentChat.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ChatMessage message = dataSnapshot.child("_messages_/_last_message_").getValue(ChatMessage.class);
                        String requestId = dataSnapshot.child(FIREBASE_DETAILS_ATTR).child("request_id").getValue(String.class);


                        Customer customer = userInfo.getValue(Customer.class);


                        String username = customer.getUsername();
                        String picture = (customer.getProfilePicture() ==null) ? "":customer.getProfilePicture();

                        CommunityChatModel communityChatModel = addChat(chatRef.getKey(),
                                FIREBASE_PRIVATE_ATTR,
                                username,
                                picture,
                                message.getMessage(),
                                (long)message.getTimestamp(),
                                message.getCounter()
                        );

                        communityChatModel.setOpponentId(opponentKey);
                        communityChatModel.setRequestId(requestId);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference refLastMessage = refCurrentChat.child("_messages_/_last_message_");
        refLastMessage.addValueEventListener(lastMessageListener);
    }


    @Override
    protected void OnStartActivity() {


        refAuthCurrentUserChats = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_USER_CHAT_REFERENCES);
        loadPrivatePendingChats();
        loadPrivateChat();
    }




    private void loadPrivatePendingChats() {

        String uid = app.getUserInformation().getUID();


        // here we will get a pending chats ref
        refPendingChat = app
                .getDatabaseChat()
                .child(FIREBASE_PENDING_CHAT_ATTR + "/" + uid + "/" + FIREBASE_PRIVATE_ATTR);


        refPendingChat.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setPendingChatToUserRef(dataSnapshot, refPendingChat);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // get a chat id  from pending chat and set a ref on chat refreance in user_info
                setPendingChatToUserRef(dataSnapshot, refPendingChat);
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
        });

    }


    private void setPendingChatToUserRef(DataSnapshot dataSnapshot, DatabaseReference refPendingChat) {
        // get a chat id  from pending chat and set a ref on chat refreance in user_info
        String chatKey = dataSnapshot.child("chat_id").getValue().toString();
        String opponentId = dataSnapshot.getKey();

        // path --> /_users_info_/[UID]/_chat_refs_/_private_/[chatKey]/
        DatabaseReference currentChatRef = refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).child(chatKey);
        currentChatRef.child(FIREBASE_OPPONENT_ID_ATTR).setValue(opponentId);
        currentChatRef.child(FIREBASE_COUNTER_PATH).setValue(0);


        // remove a ref from pending chat
        refPendingChat.child(dataSnapshot.getKey()).removeValue();
    }





    private void loadPrivateChat() {
        refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(privateSingleChatListener);

    }



    private void updateLastMessage(String chatKey, String username, String message, long time) {


        CommunityChatModel communityChatModel = communityChatModelHashMap.get(chatKey);

        if(communityChatModel == null)
            return;

        communityChatModel.setLastMsg(message);
        communityChatModel.setTimeStamp(time);
        mAdapter.notifyDataSetChanged();


    }


    private void updateCounter(String chatKey, String counter) {


        for (CommunityChatModel communityChatModel : communityUserLists) {
            if (communityChatModel.getChatKey().equals(chatKey)) {
                communityChatModel.setChatCounter(Long.parseLong(counter));
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }


    @Override
    protected void removeChatFromlist(CommunityChatModel model) {
        deletedCommunityChatModelHashMap.put(model.getChatKey(),model);
        removeFromList(model.getChatKey());
    }


    @Override
    protected void OnClickHolders(CommunityChatModel model, View v) {
        Intent i = new Intent(v.getContext(), ChatCore.class);
        i.putExtra("room_key", model.getChatKey());
        i.putExtra("room_type", model.getChatType());
        i.putExtra("room_name", model.getChatName());
        i.putExtra("room_picture", model.getUserPictureURL());

        if (model.getChatType().equals(FIREBASE_PRIVATE_ATTR))
            i.putExtra("opponent_key", model.getOpponentId());
        app.getMainAppMenuCore().startActivityForResult(i, 1);
    }




    protected CommunityChatModel addChat(String chatKey, String chatType, String chatName, String pictureURL, String lastMessage, long timeStamp, long messageNumber) {


        Log.i("--->",chatName);

        CommunityChatModel communityUserList = new CommunityChatModel();
        communityUserList.setChatKey(chatKey);
        communityUserList.setChatType(chatType);
        communityUserList.setChatName(chatName);
        communityUserList.setUserPictureURL(pictureURL);
        communityUserList.setLastMsg(lastMessage);
        communityUserList.setChatCounter(messageNumber);
        communityUserList.setTimeStamp(timeStamp);

        super.addChat(chatKey,chatType,chatName,pictureURL,lastMessage,timeStamp,messageNumber);
        return communityUserList;
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).removeEventListener(privateSingleChatListener);

    }


}
