package com.example.boxtech.skillnetwork.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.boxtech.skillnetwork.Cores.CommunityCore;
import com.example.boxtech.skillnetwork.Cores.Request.FreelancerTabCore;
import com.example.boxtech.skillnetwork.Cores.Request.RequestDescriptionTabCore;
import com.example.boxtech.skillnetwork.Fragments.ParentRequestFragments;
import com.example.boxtech.skillnetwork.Models.RequestModel;

import java.util.ArrayList;

public class RequestAdapter extends FragmentStatePagerAdapter {



    private FragmentManager fm;
    private ParentRequestFragments parentRequestFragments;
    private ArrayList<Fragment> fragments;
    private RequestModel currentRequest;

    public RequestAdapter(FragmentManager fm, RequestModel currentRequest) {
        super(fm);
        this.fm = fm;

        this.currentRequest = currentRequest;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("requestId", currentRequest.getRequestId());

        switch (position)
        {
            case 0:

                RequestDescriptionTabCore descriptionTabCore = new RequestDescriptionTabCore();
                descriptionTabCore.setArguments(bundle);

                return descriptionTabCore;
            case 1 :
                FreelancerTabCore freelancerTabCore =  new FreelancerTabCore();
                freelancerTabCore.setArguments(bundle);

                return freelancerTabCore;

        }
        return null;
    }



    public void setParentRequestFragments(ParentRequestFragments fragments)
    {
        this.parentRequestFragments = fragments;
        notifyDataSetChanged();
    }


    @Override
    public int getCount()
    {
        return 2;
    }




}
