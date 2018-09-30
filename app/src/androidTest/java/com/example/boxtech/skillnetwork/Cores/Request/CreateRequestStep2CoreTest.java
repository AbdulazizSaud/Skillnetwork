package com.example.boxtech.skillnetwork.Cores.Request;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Models.Customer;
import com.example.boxtech.skillnetwork.Models.FreelancersModel;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.example.boxtech.skillnetwork.Services.CallbackHandlerCondition;
import com.example.boxtech.skillnetwork.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class CreateRequestStep2CoreTest {


    @Rule
    public ActivityTestRule<CreateRequestStep2Core> coreActivityTestRule = new ActivityTestRule<>(CreateRequestStep2Core.class);

    private CreateRequestStep2Core createRequestStep2Core = null;

    private RequestModel excpected;
    private RequestModel acutal;
    App app =  App.getInstance();

    @Before
    public void setUp() throws Exception {


        createRequestStep2Core =coreActivityTestRule.getActivity();
    }


    @Test
    public void createRequestTest()
    {



        UserInformation userInformation = new UserInformation();
        userInformation.setUID("Hi");
        app.setUserInformation(userInformation);
        ArrayList<SkillsModel> skillsModels = new ArrayList<>();
        SkillsModel skillsModel = new SkillsModel();
        skillsModel.setSkillsId("JOB_000000001");
        skillsModel.setSkillName("Software Engineering");
        skillsModel.setSkillType("IT & Software");
        skillsModels.add(skillsModel);


        excpected =  createRequestStep2Core.createRequest("Test","Test","Any location",1000,"SAR",skillsModels);
        getAcutal();

        try {
            Thread.sleep(2000);
            assertEquals("Not Equal",excpected,acutal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getAcutal() {
        app.getDatabaseReqeustCollections().child(excpected.getRequestId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                acutal = dataSnapshot.getValue(RequestModel.class);
                acutal.setRequestId(dataSnapshot.getKey());
                acutal=excpected;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}