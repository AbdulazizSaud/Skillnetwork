package com.example.boxtech.skillnetwork.Cores.Request;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.boxtech.skillnetwork.CoresAbstract.Report;
import com.example.boxtech.skillnetwork.CoresAbstract.Request.RequestDeatils;
import com.example.boxtech.skillnetwork.Models.BidModel;
import com.example.boxtech.skillnetwork.Models.FreelancersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.DONE_STATUS;
import static com.example.boxtech.skillnetwork.Interfaces.Constants.DURING_STATUS;
import static com.example.boxtech.skillnetwork.Interfaces.Constants.WATTING_STATUS;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_USER_PENDING_REQUESTS_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_USER_REQUESTS_ATTR;


public class RequestDetailsCore extends RequestDeatils {


    @Override
    protected void makeBids(String description,float bid) {

        String reqUid = currentRequest.getRequestId();
        String UID = app.getUserInformation().getUID();
        String name = app.getUserInformation().getUsername();
        String pic = app.getUserInformation().getPictureURL();
        FreelancersModel freelancersModel = new FreelancersModel(UID,name,pic);
        BidModel bidModel = new BidModel(description,bid,false, 0);

        freelancersModel.setBid(bidModel);


        String path=  UID+"/"+FIREBASE_USER_REQUESTS_ATTR+"/"+reqUid;


        if(currentRequest.getFreelancers() != null)
        {
            currentRequest.getFreelancers().put(UID,freelancersModel);
        } else {
            HashMap<String,FreelancersModel> freelancers = new HashMap<>();
            freelancers.put(UID,freelancersModel);
            currentRequest.setFreelancers(freelancers);
        }


        app.getDatabaseReqeustCollections().child(reqUid).setValue(currentRequest);
        app.getDatabaseReqeustCollections().child(reqUid+"/timeStamp").setValue(ServerValue.TIMESTAMP);
        app.getDatabaseUsersInfo().child(path).setValue(reqUid);

    }

    @Override
    protected void requestListener() {

        Log.i("-->","mewo ");

        app.getDatabaseReqeustCollections().child(currentRequest.getRequestId()+"/status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String b = dataSnapshot.getValue()==null? WATTING_STATUS : dataSnapshot.getValue(String.class);

                if(b.equals(DURING_STATUS))
                setConfirmVisablity(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void confirmRequest() {
        app.getDatabaseReqeustCollections().child(currentRequest.getRequestId()+"/status").setValue(DONE_STATUS);

        Intent i =  new Intent(getApplicationContext(), Report.class);
        startActivity(i);
    }

}
