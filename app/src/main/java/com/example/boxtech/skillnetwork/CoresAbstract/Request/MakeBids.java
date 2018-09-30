package com.example.boxtech.skillnetwork.CoresAbstract.Request;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.example.boxtech.skillnetwork.R;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.BIDS_BID;
import static com.example.boxtech.skillnetwork.Interfaces.Constants.BIDS_DESCRIPTION;

public class MakeBids extends AppCompatActivity {


    private TextInputEditText descriptionBidsEditText;
    private TextInputEditText bidEditText;
    private Button bidBtn;

    private float constrinst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_bids);

        Intent i = getIntent();
        constrinst = i.getFloatExtra("max",-1);

        descriptionBidsEditText = (TextInputEditText) findViewById(R.id.description_edittext);
        bidEditText = (TextInputEditText) findViewById(R.id.bids_editext);
        bidBtn = (Button)findViewById(R.id.makebid_button);



        bidEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().isEmpty() || editable == null)
                    return;


                float num = Float.parseFloat(editable.toString().trim());

                if(num > constrinst)
                {
                    bidEditText.setError("You can't set value greater than"+constrinst);
                    bidEditText.setText(String.valueOf(constrinst));
                } else if (num <0)
                {
                    bidEditText.setError("You can't set value less than 0 ");
                    bidEditText.setText(String.valueOf(0));
                }
            }
        });

        bidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String descrition = descriptionBidsEditText.getText().toString().trim();
                String bid = bidEditText.getText().toString().trim();

                if(descrition.isEmpty() || bid.isEmpty())
                    return;


                Intent returnIntent = new Intent();
                returnIntent.putExtra(BIDS_DESCRIPTION,descrition);
                returnIntent.putExtra(BIDS_BID,bid);

                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }

}
