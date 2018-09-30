package com.example.boxtech.skillnetwork.Cores.Request;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.boxtech.skillnetwork.Models.BidModel;
import com.example.boxtech.skillnetwork.Models.FreelancersModel;
import com.example.boxtech.skillnetwork.Models.RequestModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FreelancerTabCoreTest {


    public ActivityTestRule<RequestDetailsCore> coreActivityTestRule = new ActivityTestRule<>(RequestDetailsCore.class);
    private FreelancerTabCore freelancerTabCore = null;



    @Before
    public void setUp() throws Exception {

        freelancerTabCore = new FreelancerTabCore();

        RequestModel requestModel =  new RequestModel();
        requestModel.setRequestId("Test");
        freelancerTabCore.currentRequest = requestModel;
        freelancerTabCore.mAdapter = freelancerTabCore.createAdapter();
    }

    @Test
    public void retrieveFreelancersRealtime() {
        freelancerTabCore.retrieveFreelancersRealtime();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<FreelancersModel> array1 = new ArrayList<>();

        FreelancersModel freelancersModel = new FreelancersModel();
        freelancersModel.setUid("TestUser");
        array1.add(freelancersModel);
        ArrayList<FreelancersModel> array2 = freelancerTabCore.bidsFreelancer;

        String expected = array1.get(0).getUid();
        String acutal = array1.get(0).getUid();

        assertEquals("Not Equal",expected,acutal);

    }
}