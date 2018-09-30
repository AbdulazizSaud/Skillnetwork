package com.example.boxtech.skillnetwork.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.boxtech.skillnetwork.Cores.CommunityCore;
import com.example.boxtech.skillnetwork.Cores.ProfileCore;
import com.example.boxtech.skillnetwork.Cores.RequestsCore;
import com.example.boxtech.skillnetwork.Fragments.ParentRequestFragments;

import java.util.ArrayList;

public class MainMenuAdapter extends FragmentStatePagerAdapter
{

    private FragmentManager fm;
    private ParentRequestFragments parentRequestFragments;
    private ArrayList<Fragment> fragments;

    public MainMenuAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new CommunityCore();
            case 1 :
                return new RequestsCore();
            case 2:
                return new ProfileCore();

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
        return 3;
    }
}
