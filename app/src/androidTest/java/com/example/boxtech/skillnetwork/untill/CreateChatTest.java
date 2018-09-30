package com.example.boxtech.skillnetwork.untill;

import android.util.Log;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateChatTest {

    private App app;
    private CreateChat createChat;
    boolean expected = true;
    boolean acutal = false;

    @Before
    public void setUp() throws Exception {
        app = App.getInstance();

        UserInformation userInformation = new UserInformation();

        userInformation.setType(Constants.ACCOUNT_TYPE_FREELANCER);
        userInformation.setUID("ID-TEST01");
        userInformation.setUsername("Test1");
        userInformation.setPictureURL("");
        app.setUserInformation(userInformation);
    }

    @Test
    public void createPrivateChat() {
        createChat = new CreateChat("TestChat");
        createChat.createPrivateChat(null,"ID-TEST02","Test02","","Test");
        app.getDatabaseChatPrivate().child("TestChat/_info_/_users_").child("ID-TEST02").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // true
                acutal = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            Thread.sleep(2500);
            assertEquals("Not Equal",expected,acutal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}