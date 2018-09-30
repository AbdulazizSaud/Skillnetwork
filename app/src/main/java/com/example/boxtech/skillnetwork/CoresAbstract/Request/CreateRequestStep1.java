package com.example.boxtech.skillnetwork.CoresAbstract.Request;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.boxtech.skillnetwork.R;


public abstract class CreateRequestStep1 extends AppCompatActivity {


    private AutoCompleteTextView titleEditText;
    private TextInputEditText descriptionEditText;

    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request_step1);

        titleEditText = (AutoCompleteTextView) findViewById(R.id.title_new_request);
        descriptionEditText = (TextInputEditText) findViewById(R.id.description_edittext_new_request);
        nextBtn = (Button)findViewById(R.id.next_button_new_request);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toStepTwo();
            }
        });
    }



    public String getTitleText() {
        return titleEditText.getText().toString();
    }

    public String getDescriptionText() {
        return descriptionEditText.getText().toString();
    }


    protected abstract void OnStartActivity();
    protected abstract void toStepTwo();
}
