package com.example.boxtech.skillnetwork.Models;

public class BidModel {

    private String bidDescription;
    private float bid;
    private boolean status;
    private long timeStamp;

    public BidModel() {
    }

    public BidModel(String bidDescription, float bid, boolean status, long timestamp) {
        this.bidDescription = bidDescription;
        this.bid = bid;
        this.status = status;
        this.timeStamp = timestamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getBidDescription() {
        return bidDescription;
    }

    public void setBidDescription(String bidDescription) {
        this.bidDescription = bidDescription;
    }

    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
