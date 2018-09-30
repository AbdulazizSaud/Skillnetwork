package com.example.boxtech.skillnetwork.Models;


import android.os.Parcel;
import android.os.Parcelable;

public abstract class UserModel implements Parcelable {

    protected String uid,username ,profilePicture;

    public UserModel(){}

    public UserModel(String uid, String username, String profilePicture) {
        this.username = username;
        this.uid = uid;
        this.profilePicture = profilePicture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(uid);
        dest.writeString(profilePicture);
    }


    @Override
    public int describeContents() {
        return 0;
    }

}
