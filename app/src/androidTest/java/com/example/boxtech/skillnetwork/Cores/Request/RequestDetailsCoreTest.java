package com.example.boxtech.skillnetwork.Cores.Request;

import android.support.test.rule.ActivityTestRule;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Models.BidModel;
import com.example.boxtech.skillnetwork.Models.Customer;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestDetailsCoreTest {


    @Rule
    public ActivityTestRule<RequestDetailsCore> requestDetailsCoreActivityTestRule;


    private RequestDetailsCore requestDetailsCore = null;

    private BidModel actual;
    private  UserInformation userInformation;
    private RequestModel requestModel;

    @Before
    public void setUp() throws Exception {

        userInformation = new UserInformation();
        userInformation.setUID("Test1110");
        userInformation.setUsername("MakeBid");
        userInformation.setPictureURL("");
        userInformation.setType(Constants.ACCOUNT_TYPE_FREELANCER);
        App app = App.getInstance();

        app.setUserInformation(userInformation);

        requestModel = new RequestModel();
        requestModel.setRequestId("Test");
        requestModel.setOwner(new Customer());

        requestDetailsCoreActivityTestRule=new ActivityTestRule<>(RequestDetailsCore.class);
        requestDetailsCore = requestDetailsCoreActivityTestRule.getActivity();

    }


    @Test
    public void makeBidTest() {

        BidModel expected = new BidModel("makeBid",1000,false, 0);


        App.getInstance().getDatabaseReqeustCollections().child(requestModel.getRequestId()+"/freelancers/"+userInformation.getUID()).child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                actual = dataSnapshot.getValue(BidModel.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            Thread.sleep(2500);
            assertEquals("Not Equal",expected,actual);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}