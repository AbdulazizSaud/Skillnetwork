package com.example.boxtech.skillnetwork.Cores;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.Auth.LoginCore;
import com.example.boxtech.skillnetwork.CoresAbstract.MainMenu;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.example.boxtech.skillnetwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainMenuCore extends MainMenu implements FirebasePaths{

    private App app;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void OnStartActivity() {

        app =  App.getInstance();
        app.setMainAppMenuCore(this);
        mAuth = app.getAuth();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is sign out
                   startActivity(new Intent(getApplicationContext(),LoginCore.class));
                } else if(!user.isEmailVerified()){
                    Toast.makeText(getApplicationContext(),"You need to verify your account",Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(),LoginCore.class));
                } else {
                    setupUserInformation(user);
                }
            }
        };
    }



    private void setupUserInformation(final FirebaseUser user){



        app.getDatabaseUsersInfo().child(user.getUid()+"/"+ FirebasePaths.FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);


                if(userInformation == null) {
                    app.logout(getApplication());
                    return;
                }
                userInformation.setUID(user.getUid());
                userInformation.setUserEmail(user.getEmail());

                app.getTimeStamp().setUseruid(user.getUid());
                app.setUserInformation(userInformation);

                if (!App.isWelcomed)
                {
                    welcomeMessage(app.getUserInformation().getUsername());
                    App.isWelcomed=true;

                    isDone = true;


                    if(app.getUserInformation().getType().equals(Constants.ACCOUNT_TYPE_FREELANCER))
                    {
                        Intent i = new Intent(getApplicationContext(), AddSkillCore.class);
                        i.putExtra("CameFrom", "Login");
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);
                        //finish();
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        app.getDatabaseSkills().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String id = snapshot.getKey();
                    String jobName = snapshot.child("jobName").getValue(String.class);
                    String type = snapshot.child("Type").getValue(String.class);
                    app.addSkill(id,jobName,type);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    @Override
    protected void onStop() {
        // remove authstate.listenr to firebase auth
        mAuth.removeAuthStateListener(authStateListener);
        super.onStop();

    }

    @Override
    protected void onStart(){
        // add authstate.listenr to firebase auth

        mAuth.addAuthStateListener(authStateListener);
        super.onStart();

    }



    private boolean isInfoValid(DataSnapshot dataSnapshot) {
        return dataSnapshot.hasChild(FIREBASE_USERNAME_PATH) &&  dataSnapshot.hasChild(FIREBASE_PICTURE_URL_PATH);
    }


}
