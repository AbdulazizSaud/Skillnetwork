package com.example.boxtech.skillnetwork.Cores;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.boxtech.skillnetwork.Cores.Request.RequestDetailsCore;
import com.example.boxtech.skillnetwork.CoresAbstract.Request.RequestDeatils;
import com.example.boxtech.skillnetwork.Fragments.RequestsFragment;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.DONE_STATUS;

public class RequestsCore extends RequestsFragment implements FirebasePaths{

    @Override
    protected void OnStartActivity() {
        loadRequestList();
    }

    @Override
    protected void OnClickHolders(RequestModel model, View v) {
        Intent i = new Intent(v.getContext(), RequestDetailsCore.class);
        // here we will send the data;
        i.putExtra("requestId",model.getRequestId());
        v.getContext().startActivity(i);
    }



    private void loadRequestList() {
        DatabaseReference requestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_USER_REQUESTS_ATTR);


        requestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setRequestEvents(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setRequestEvents(dataSnapshot);
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


    private void setRequestEvents(DataSnapshot dataSnapshot) {


        final String requestId = dataSnapshot.getKey();
        // Load games in auto complete edititext : search request
        app.clearAdapter();
        app.getDatabaseReqeustCollections().child(requestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);

                if(!requestModel.getStatus().equals(DONE_STATUS))
                addRequest(requestModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void toRequest() {

    }


    @Override
    protected void removeChatFromlist(RequestModel model) {

    }


}
