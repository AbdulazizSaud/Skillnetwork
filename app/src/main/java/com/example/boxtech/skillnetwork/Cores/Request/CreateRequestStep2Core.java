package com.example.boxtech.skillnetwork.Cores.Request;


import android.content.Intent;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.boxtech.skillnetwork.CoresAbstract.Request.CreateRequestStep2;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.example.boxtech.skillnetwork.Models.Customer;
import com.example.boxtech.skillnetwork.Models.FreelancersModel;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateRequestStep2Core extends CreateRequestStep2 implements FirebasePaths {



    private DatabaseReference collectionsRef;
    private DatabaseReference categoriesRef;
    private String id;



    @Override
    protected void OnStartActivity() {
        collectionsRef = app.getDatabaseReqeustCollections();
        categoriesRef  = app.getDatabaseRequestCategories();
         id = collectionsRef.push().getKey();


    }

    @Override
    public RequestModel createRequest(String title,String description,String location,float budget,String currency,ArrayList<SkillsModel> skillsModels) {



        RequestModel requestModel = new RequestModel();

        requestModel.setRequestTitle(title);
        requestModel.setRequestDescription(description);
        requestModel.setRequestId(id);
        requestModel.setRequestLocation(location);
        requestModel.setRequestBudget(budget);
        requestModel.setRequestCurrency(currency);
        ArrayList<SkillsModel> skillsModelArrayList = skillsModels;
        requestModel.setSkillsModels(skillsModelArrayList);
        requestModel.setFreelancers(new HashMap<String, FreelancersModel>());



        String uid = app.getUserInformation().getUID();
        String username = app.getUserInformation().getUsername();
        String picture = app.getUserInformation().getPictureURL();

        requestModel.setOwner(new Customer(uid,username,picture));

        collectionsRef.child(id).setValue(requestModel);
        collectionsRef.child(id+"/"+FIREBASE_REQUEST_TIME_STAMP_ATTR).setValue(ServerValue.TIMESTAMP);

       for (SkillsModel skill :skillsModelArrayList)
       {
           categoriesRef.child(skill.getSkillType()).child(skill.getSkillName()+"/"+id).setValue(id);
       }


       app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()+"/"+FIREBASE_USER_REQUESTS_ATTR+"/"+id).setValue(id);
       app.addRequest(requestModel);

        return requestModel;
    }
}
