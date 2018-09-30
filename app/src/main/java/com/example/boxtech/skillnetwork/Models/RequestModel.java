package com.example.boxtech.skillnetwork.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.WATTING_STATUS;

public class RequestModel {


    private String requestId;
    private String requestTitle;
    private String requestDescription;
    private String requestLocation;

    private String requestCurrency;
    private float requestBudget =0;
    private long timeStamp;

    private String status = WATTING_STATUS;


    private Customer owner;
    private HashMap<String,FreelancersModel> freelancers = new HashMap<>();

    private ArrayList<SkillsModel> skillsModels = new ArrayList<>();


    public RequestModel() {

    }



    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getRequestLocation() {
        return requestLocation;
    }

    public void setRequestLocation(String requestLocation) {
        this.requestLocation = requestLocation;
    }

    public String getRequestCurrency() {
        return requestCurrency;
    }

    public void setRequestCurrency(String requestCurrency) {
        this.requestCurrency = requestCurrency;
    }

    public float getRequestBudget() {
        return requestBudget;
    }

    public void setRequestBudget(float requestBudget) {
        this.requestBudget = requestBudget;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }


    public ArrayList<SkillsModel> getSkillsModels() {
        return skillsModels;
    }

    public void setSkillsModels(ArrayList<SkillsModel> skillsModels) {
        this.skillsModels = skillsModels;
    }

    public HashMap<String, FreelancersModel> getFreelancers() {
        return freelancers;
    }


    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setFreelancers(HashMap<String, FreelancersModel> freelancers) {
        this.freelancers = freelancers;
    }

}
