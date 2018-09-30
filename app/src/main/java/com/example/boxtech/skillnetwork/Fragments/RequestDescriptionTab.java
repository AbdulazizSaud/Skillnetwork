package com.example.boxtech.skillnetwork.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.R;

import java.util.ArrayList;


public class RequestDescriptionTab extends Fragment {


    protected App app;
    protected RequestModel currentRequest;

    private TextView titleTextView,descriptionTextView,budgetTextView,locationTextView,skillsTextView;

    public RequestDescriptionTab() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        app = App.getInstance();
        String requestId = getArguments().getString("requestId");
        currentRequest = app.getRequestById(requestId);

        View v = inflater.inflate(R.layout.fragment_request_description, container, false);;

        titleTextView = (TextView) v.findViewById(R.id.title_request);
        descriptionTextView  = (TextView) v.findViewById(R.id.description_request);
        budgetTextView = (TextView)v.findViewById(R.id.budget_request);
        skillsTextView = (TextView)v.findViewById(R.id.skills_request);
        locationTextView = (TextView)v.findViewById(R.id.location_request);

        setupInformation();


        return v;
    }

    private void setupInformation()
    {
        titleTextView.setText(currentRequest.getRequestTitle());
        descriptionTextView.setText(currentRequest.getRequestDescription());


        String currency = (currentRequest.getRequestCurrency().equals("USD")) ? "$": "SR";

        budgetTextView.setText(currentRequest.getRequestBudget() +currency);
        locationTextView.setText(currentRequest.getRequestLocation());

        String skills = "";

        ArrayList<SkillsModel> skillsModels =  currentRequest.getSkillsModels();

        for(SkillsModel skill : skillsModels)
            skills+=skill.getSkillName()+",";

        skillsTextView.setText(skills);


    }





}
