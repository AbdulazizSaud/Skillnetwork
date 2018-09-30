package com.example.boxtech.skillnetwork.CoresAbstract.Request;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.Adapters.RequestAdapter;
import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.R;
import com.example.boxtech.skillnetwork.Services.CallbackHandlerCondition;
import com.example.boxtech.skillnetwork.Services.HandlerCondition;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import static com.example.boxtech.skillnetwork.Interfaces.Constants.BIDS_BID;
import static com.example.boxtech.skillnetwork.Interfaces.Constants.BIDS_DESCRIPTION;

public abstract class RequestDeatils extends AppCompatActivity {



    public App app;
    protected RequestAdapter menuPagerAdapter;
    protected ViewPager viewPagerMenu;
    protected BottomBar topBar;
    protected FloatingActionButton bidsButton;
    public RequestModel currentRequest;
    protected boolean isDone=true;

    private boolean changeBar = true;
    private ImageView backBtn;
    private ImageView confirmBtn;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_deatils);

        app = App.getInstance();

        Intent i = getIntent();



        String id = i.getStringExtra("requestId");
        currentRequest = app.getRequestById(id);


        viewPagerMenu = (ViewPager) findViewById(R.id.request_view_pager);
        // Prevent destroyin old fragments : it was 4
        viewPagerMenu.setOffscreenPageLimit(3);
        topBar = (BottomBar) findViewById(R.id.request_bar);
        menuPagerAdapter = new RequestAdapter(getSupportFragmentManager(),currentRequest);
        backBtn = (ImageView)findViewById(R.id.close_chat_imageview);
        confirmBtn = (ImageView)findViewById(R.id.confirm_btn);

        bidsButton = (FloatingActionButton)findViewById(R.id.add_bids_btn);


        bidsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMakeBid();
            }
        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.cleanPassRequest();
                finish();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmRequest();

            }
        });

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {

                if (isDone) activateMainAppMenu(savedInstanceState);
                return isDone;
            }
        };

        new HandlerCondition(callback,0);


        if(app.getUserInformation().getType().equals(Constants.ACCOUNT_TYPE_FREELANCER))
        {

            if(currentRequest.getFreelancers() !=null)
                if(currentRequest.getFreelancers().containsKey(app.getUserInformation().getUID()))
                    return;

                    bidsButton.setVisibility(View.VISIBLE);
        }

        requestListener();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void activateMainAppMenu( Bundle savedInstanceState) {

        viewPagerMenu.setAdapter(menuPagerAdapter);
        setupBottmBar(savedInstanceState);
    }


    private void setupBottmBar(Bundle savedInstanceState)
    {

        topBar.selectTabAtPosition(0);

        viewPagerMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                topBar.selectTabAtPosition(position, true);
                topBar.setInActiveTabColor(0x000033);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        topBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                changeBar = false;
                switch (tabId) {
                    case R.id.tab_chat_community:
                        viewPagerMenu.setCurrentItem(0, true);
                        break;
                    case R.id.tab_request:
                        viewPagerMenu.setCurrentItem(1, true);
                        break;
                }
            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String des =data.getStringExtra(BIDS_DESCRIPTION);
                float bid  = Float.valueOf(data.getStringExtra(BIDS_BID));

                makeBids(des,bid);

                Toast.makeText(getApplicationContext(),"Your bid has been added",Toast.LENGTH_SHORT).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    public void goMakeBid()
    {
        Intent i = new Intent(getApplicationContext(), MakeBids.class);
        i.putExtra("max",currentRequest.getRequestBudget());
        startActivityForResult(i, 1);
    }

    public void setConfirmVisablity(int visablity){
        confirmBtn.setVisibility(visablity);
    }

    protected abstract void makeBids(String description,float bid);
    protected abstract void requestListener();
    protected abstract void confirmRequest();


}





