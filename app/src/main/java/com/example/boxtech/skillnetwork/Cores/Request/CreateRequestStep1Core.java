package com.example.boxtech.skillnetwork.Cores.Request;

import android.content.Intent;
import android.util.Log;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.CoresAbstract.Request.CreateRequestStep1;
import com.example.boxtech.skillnetwork.Models.RequestModel;


public class CreateRequestStep1Core extends CreateRequestStep1 {
    @Override
    protected void OnStartActivity() {

    }

    @Override
    protected void toStepTwo() {
        RequestModel requestModel = new RequestModel();

        requestModel.setRequestTitle(getTitleText());
        requestModel.setRequestDescription(getDescriptionText());

        App.getInstance().setPassRequest(requestModel);
        Intent intent = new Intent(this,CreateRequestStep2Core.class);

        startActivity(intent);



    }
}
