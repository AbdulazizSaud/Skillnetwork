package com.example.boxtech.skillnetwork.Cores;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.Request.RequestDetailsCore;
import com.example.boxtech.skillnetwork.CoresAbstract.Search;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.DONE_STATUS;
import static com.example.boxtech.skillnetwork.Interfaces.Constants.DURING_STATUS;

public class SearchCore extends Search {


    @Override
    protected void OnStartActivity() {

        app.getDatabaseReqeustCollections().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);

                if(requestModel.equals(DURING_STATUS) ||requestModel.equals(DONE_STATUS) )
                    return;

                requestUserLists.add(requestModel);
                app.addRequest(requestModel);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);
                if(requestModel.equals(DURING_STATUS) ||requestModel.equals(DONE_STATUS) )
                    return;

                requestUserLists.add(requestModel);
                app.addRequest(requestModel);
                mAdapter.notifyDataSetChanged();

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
    protected void OnClickHolders(RequestModel model, View v) {

        Intent i = new Intent(v.getContext(), RequestDetailsCore.class);
        // here we will send the data;
        i.putExtra("requestId",model.getRequestId());



        v.getContext().startActivity(i);

    }
}
