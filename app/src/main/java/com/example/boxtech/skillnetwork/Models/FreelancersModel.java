package com.example.boxtech.skillnetwork.Models;


import android.annotation.SuppressLint;
import android.os.Parcel;

import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class FreelancersModel extends UserModel {

    private ArrayList<SkillsModel> skillsModel;
    private BidModel bid;



    public FreelancersModel()
    {

    }

    public FreelancersModel(String uid, String username, String profilePicture) {
        super(uid,username,profilePicture);
        skillsModel = new ArrayList<>();
    }

    public void addSkills(SkillsModel skills)
    {
        skillsModel.add(skills);
    }

    public BidModel getBid() {
        return bid;
    }

    public void setBid(BidModel bid) {
        this.bid = bid;
    }
}
