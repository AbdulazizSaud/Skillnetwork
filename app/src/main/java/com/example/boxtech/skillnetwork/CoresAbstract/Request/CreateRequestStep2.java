package com.example.boxtech.skillnetwork.CoresAbstract.Request;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.boxtech.skillnetwork.Adapters.SpinnerAdapter;
import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.Request.RequestDetailsCore;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public abstract class CreateRequestStep2 extends AppCompatActivity {



    protected App app;

    public AutoCompleteTextView skillsEditText;
    public TextInputEditText budgetEditText;
    public MaterialBetterSpinner currencySipnner;
    public RadioGroup radioGroup;

    private AppCompatButton postBtn;

    protected ArrayList<String> currcenyList;
    protected ArrayAdapter currcenyAdapter;


    protected RequestModel requestModel;


    protected ArrayList<String> skillsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request_step2);

        app = App.getInstance();

        requestModel = app.getPassRequest();




        skillsEditText = (AutoCompleteTextView) findViewById(R.id.skills_required_new_request);
        budgetEditText = (TextInputEditText) findViewById(R.id.budget_new_request) ;
        currencySipnner = (MaterialBetterSpinner)findViewById(R.id.currency_new_request);
        radioGroup = (RadioGroup) findViewById(R.id.locations_radiogroup);
        postBtn = (AppCompatButton) findViewById(R.id.request_button_new_request);


        skillsList  = new ArrayList<>();


        currcenyList = new ArrayList<>();

        currcenyList.add("SAR");
        currcenyList.add("USD");

        currcenyAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item, currcenyList);
        currcenyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        currencySipnner.setAdapter(currcenyAdapter);


        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                app.reloadSkills();
                skillsEditText.setAdapter(app.getSkillAdapter());
            }
        }.start();


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestModel req = createRequest(requestModel.getRequestTitle(),requestModel.getRequestDescription(),getLocation(),getBudget(),getCurrency(),getSkills());
                Intent i = new Intent(getApplicationContext(), RequestDetailsCore.class);
                i.putExtra("requestId",req.getRequestId());
                startActivity(i);
                finish();

            }

        });

        OnStartActivity();
    }






    protected String getLocation()
    {

        int selectedBtnId  = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedBtn  = (RadioButton)findViewById(selectedBtnId);
        return selectedBtn.getText().toString().trim();
    }

    protected ArrayList<SkillsModel> getSkills(){


        ArrayList<SkillsModel> skillsModelArrayList = new ArrayList<>();
        String[] skills = skillsEditText.getText().toString().split(",");

        for(String skill : skills)
        {
           SkillsModel skillsModel =  app.getSkillManager().getSkillByName(skill);

           if(skillsModel !=null)
           {
               skillsModelArrayList.add(skillsModel);
           }
        }

        return skillsModelArrayList;
    }

    protected String getCurrency()
    {
        return currencySipnner.getText().toString().trim();
    }

    protected float getBudget()
    {
        float num = (!budgetEditText.getText().toString().isEmpty())? Float.parseFloat(budgetEditText.getText().toString()) : -1;
        return num;
    }



    protected abstract void OnStartActivity();
    public abstract RequestModel  createRequest(String title,String description,String location,float budget,String currency,ArrayList<SkillsModel> skillsModels);

}
