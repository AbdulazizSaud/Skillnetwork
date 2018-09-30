package com.example.boxtech.skillnetwork.Cores.Auth;


import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.CoresAbstract.Auth.Signup;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SignupCore extends Signup implements FirebasePaths{


    private FirebaseAuth appAuth;

    @Override
    protected void OnStartActivity() {
        appAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void signup(final String email, final String username, final String password, final String accountType, final String country) {

        appAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            FirebaseUser user = appAuth.getCurrentUser();
                           // insertInfoToDatabase(user.getUid(),email,username.toLowerCase());

                           // success message
                            // switch to main AppMenu
                            app.sendEmailVerification(user,email);
                            insertInfoToDatabase(user.getUid(),email,username,accountType,country);

                            toLogin();

                        } else {
                            // failed message
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    private void insertInfoToDatabase(final String UID, final String email, final String username, String type, String country) {

        final DatabaseReference usersInfoRef = app.getDatabaseUsersInfo().child(UID).child(FIREBASE_DETAILS_ATTR);
        UserInformation userInformation = new UserInformation();

        userInformation.setBio("");
        userInformation.setType(type);
        userInformation.setUserEmail(email);
        userInformation.setPictureBitMap(null);
        userInformation.setPictureURL("");
        userInformation.setUsername(username);
        userInformation.setCountry(country);
        // user info
        usersInfoRef.setValue(userInformation);
        // user name list
        app.getDatabaseUserNames().child(username).setValue(UID);

    }
    @Override
    protected void checkUsername(String value) {

        app.getDatabaseUserNames().child(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currenCheckingStatus = (dataSnapshot.exists()) ? USER_EXIST : USER_NOT_EXIST;
                checkUserCallBack();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
