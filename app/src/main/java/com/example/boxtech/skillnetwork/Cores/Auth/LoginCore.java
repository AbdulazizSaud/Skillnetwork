package com.example.boxtech.skillnetwork.Cores.Auth;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.Cores.AddSkillCore;
import com.example.boxtech.skillnetwork.Cores.MainMenuCore;
import com.example.boxtech.skillnetwork.CoresAbstract.AddSkill;
import com.example.boxtech.skillnetwork.CoresAbstract.Auth.Login;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.example.boxtech.skillnetwork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginCore extends Login{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(authStateListener);
        super.onStart();

    }

    @Override
    protected void onStop() {
        mAuth.removeAuthStateListener(authStateListener);
        super.onStop();
    }

    // this method will do some init to check if user is authenticated , if yes .. it will switch to CustomerMenu
    @Override
    public void OnStartActivity() {
        mAuth = app.getAuth();
        mFirebaseUser = mAuth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if( user.isEmailVerified()) {
                        app.getUserInformation().setUID(user.getUid());
                        toMainMenu();
                    }
                } else {
                    showProgress(false);
                }
            }
        };

    }


    @Override
    protected void login(final String username, String password) {

        if (!username.isEmpty() && !password.isEmpty()) {
            loadingDialog(true);


            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(LoginCore.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();

                        if(!mAuth.getCurrentUser().isEmailVerified())
                        {
                            loadingDialog(false);

                        } else {


                            app.getDatabaseUsersInfo().child(uid+"/"+ FirebasePaths.FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                                    app.setUserInformation(userInformation);
                                    toMainMenu();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        }

                    } else {
                        // results if it's failed
                        loadingDialog(false);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }



    protected void toMainMenu() {

            Intent i = new Intent(getApplicationContext(), MainMenuCore.class);
            startActivity(i);

    }

}
