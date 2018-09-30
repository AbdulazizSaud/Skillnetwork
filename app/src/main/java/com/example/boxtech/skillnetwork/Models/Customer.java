package com.example.boxtech.skillnetwork.Models;


import android.annotation.SuppressLint;

@SuppressLint("ParcelCreator")
public class Customer extends UserModel {



    public Customer()
    {

    }
    public Customer(String uid, String username, String profilePicture) {
        super(uid, username, profilePicture);
    }



}
