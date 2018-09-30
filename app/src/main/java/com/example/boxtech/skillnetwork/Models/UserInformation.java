package com.example.boxtech.skillnetwork.Models;


import android.graphics.Bitmap;

import java.util.HashMap;

public class UserInformation {

    private String username,pictureURL,UID , userEmail,bio,type,country;

    private Bitmap pictureBitMap;

    public UserInformation(){}

    //
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPictureURL() {
        return pictureURL;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Bitmap getPictureBitMap() {
        return pictureBitMap;
    }

    public void setPictureBitMap(Bitmap pictureBitMap) {
        this.pictureBitMap = pictureBitMap;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
