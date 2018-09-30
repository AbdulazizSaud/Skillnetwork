package com.example.boxtech.skillnetwork.CoresAbstract;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.boxtech.skillnetwork.Adapters.ChatAdapter;
import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Fragments.ChatMessage;
import com.example.boxtech.skillnetwork.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;



import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_PRIVATE_ATTR;

public abstract class Chat extends AppCompatActivity {

    protected enum TYPE {
        PUBLIC,PRIVATE
    }


    protected App app;


    // layout contents
    protected EditText messageET;


    protected ImageView emojiBtn , closeChatImageView;
    protected ImageButton sendBtn;

    protected View rootView;
    protected ListView messagesContainer;


    protected CircleImageView roomPictureImageView;
    protected TextView roomNameTextView;
    protected TextView roomSubtitleTextView;

    // adapter impalement
    protected ChatAdapter adapter;
    protected ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();
    protected HashMap<String,ChatMessage> chatMessages = new HashMap<String,ChatMessage>();


    protected TYPE chatType;


    protected String chatRoomKey,roomName = null, roomPictureUrl = null, chatRoomType = null;
    protected String opponentId;


    // Variables for add friend menu
    protected boolean isFriend = false ;
    protected boolean isDone = false;
    protected String opponentName = "";



    @Override
    protected void onStart() {
        super.onStart();
       // App.chatActivityIsActive = true ;
    }

    @Override
    protected void onStop() {
        super.onStop();
       // App.chatActivityIsActive = false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        // Scren orientation :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);






        app = App.getInstance();

        //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        chatRoomType = i.getStringExtra("room_type");
        roomName = i.getStringExtra("room_name");
        roomPictureUrl = i.getStringExtra("room_picture");
        opponentId = i.getStringExtra("friend_key");

        Log.i("===========>","FIRST :"+opponentId);

        chatType = (chatRoomType.equals(FIREBASE_PRIVATE_ATTR)) ? TYPE.PRIVATE:TYPE.PUBLIC;

        // Users toolbar :
        Toolbar toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        initControls();
        // set up chat app mechanisms
        setupChat();
    }


    // this method to set up a chat layout containts
    private void initControls() {

        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        // init a layout contatins
        rootView = findViewById(R.id.chatContainer);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        messageET.setTypeface(playregular);

        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);
        emojiBtn = (ImageView) findViewById(R.id.emojiBtn);

        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        roomNameTextView = (TextView)findViewById(R.id.title_users_toolbar);
        roomSubtitleTextView = (TextView)findViewById(R.id.subtitle_users_toolbar);
        roomSubtitleTextView.setText("");
        roomPictureImageView = (CircleImageView)findViewById(R.id.user_in_toolbar_imageview);


        closeChatImageView = (ImageView) findViewById(R.id.close_chat_imageview);

        // add some dummy texts for test
        // loadDummyHistory();


        // implement a send button to send text
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                sendMessageToFirebase(messageText);

            }
        });

        // implement text watcher
        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                // sendBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sendiconmessagein));
                sendBtn.setImageResource(R.drawable.ic_send_focused_24dp);
            }
        });


        // close chat :
        closeChatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // set up game providers recyclerview
//        setUpGamesProvidersRecyclerView();


    }

    // this method for adding new message to adapter and display it
    protected void addMessage(ChatMessage message) {

        //check if this message is empty
        if (isMessageEmpty(message.getMessage()) ||  chatMessages.containsKey(message.getId())) {
            return;
        }

        // add to adapter and display it
        chatMessages.put(message.getId(),message);
        adapter.add(message);
        adapter.notifyDataSetChanged();

        // scroll the message layout container
        scroll();

    }

    // this method for send message , execute only when a user click on send button
    protected boolean sendMessage(String message) {


        sendBtn.setImageResource(R.drawable.ic_send_not_focused_24dp);

        // check if the message is empty
        if(isMessageEmpty(message))
            return false;
        messageET.setText("");

        return true;
    }




    // check if message is empty
    private boolean isMessageEmpty(String message) {
        return message.equals("\\s++") ||message.equals("")||message ==null || TextUtils.isEmpty(message);
    }

    private void scroll() {

        messagesContainer.setSelection(messagesContainer.getCount());
    }



    protected void setRoomDetails(String roomName,String roomPicture) {

        roomNameTextView.setText(roomName);

        roomPictureImageView.setImageResource(R.drawable.profile_default_photo);
        app.loadingImage(getApplication(),roomPictureImageView,roomPicture);

    }

    public void addUserToSubtitle(String username)
    {
        String currentSub = roomSubtitleTextView.getText().toString().trim();
        if(!currentSub.isEmpty())
            currentSub+=" , "+username;
        else
            currentSub+=username;

        roomSubtitleTextView.setText(currentSub);
    }



    public void leaveMessage(String username,String message)
    {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId("");
        chatMessage.setUsername("");
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(0);

        // add to adapter and display it
        chatMessages.put("",chatMessage);
        adapter.add(chatMessage);
        adapter.notifyDataSetChanged();
    }

    // this abstract method is for implements the chat mechinsim
    protected abstract void setupChat();
    protected abstract void sendMessageToFirebase(String message);




}
