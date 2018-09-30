package com.example.boxtech.skillnetwork.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boxtech.skillnetwork.App.App;

public class ParentRequestFragments extends Fragment {

    protected App app ;
    protected boolean hasReq;


    public ParentRequestFragments() {
        // Required empty public constructor
        app = App.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getView();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
