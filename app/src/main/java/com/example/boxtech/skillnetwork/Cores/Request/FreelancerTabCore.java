package com.example.boxtech.skillnetwork.Cores.Request;


import android.view.View;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Fragments.FreelancerTab;
import com.example.boxtech.skillnetwork.Models.FreelancersModel;
import com.example.boxtech.skillnetwork.untill.CreateChat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.DURING_STATUS;

public class FreelancerTabCore extends FreelancerTab {


    private DatabaseReference requestRef;
    private DatabaseReference freelancersRef;

    @Override
    protected void OnStartActivity() {
        retrieveFreelancersRealtime();
    }


    public void retrieveFreelancersRealtime()
    {

        app = App.getInstance();
        requestRef = app.getDatabaseReqeustCollections().child(currentRequest.getRequestId());

        freelancersRef = requestRef.child("freelancers");

        // do real time adding

        freelancersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                FreelancersModel freelancersModel = dataSnapshot.getValue(FreelancersModel.class);
                addBidders(freelancersModel);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FreelancersModel freelancersModel = dataSnapshot.getValue(FreelancersModel.class);
                addBidders(freelancersModel);
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
    @Override
    protected void OnClickHolders(FreelancersModel model, View v) {

    }

    @Override
    protected void OnClickHire(FreelancersModel model) {



        CreateChat createChat;
        createChat = new CreateChat();
        createChat.createPrivateChat(getContext(), model.getUid(), model.getUsername(), model.getProfilePicture(),currentRequest.getRequestId());

        model.getBid().setStatus(true);
        currentRequest.setStatus(DURING_STATUS);
        requestRef.setValue(currentRequest);



    }
}
