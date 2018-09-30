package com.example.boxtech.skillnetwork.CoresAbstract;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.boxtech.skillnetwork.Adapters.MainMenuAdapter;
import com.example.boxtech.skillnetwork.Cores.CommunityCore;
import com.example.boxtech.skillnetwork.Cores.ProfileCore;
import com.example.boxtech.skillnetwork.Cores.RequestsCore;
import com.example.boxtech.skillnetwork.Fragments.ParentRequestFragments;
import com.example.boxtech.skillnetwork.R;
import com.example.boxtech.skillnetwork.Services.CallbackHandlerCondition;
import com.example.boxtech.skillnetwork.Services.HandlerCondition;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

public abstract class MainMenu extends AppCompatActivity {

    protected MainMenuAdapter menuPagerAdapter;
    protected ViewPager viewPagerMenu;
    protected BottomBar bottomBar;

    protected boolean isDone=false;
    private boolean changeBar = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewPagerMenu = (ViewPager) findViewById(R.id.view_pager);
        // Prevent destroyin old fragments : it was 4
        viewPagerMenu.setOffscreenPageLimit(3);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        menuPagerAdapter = new MainMenuAdapter(getSupportFragmentManager());


        bottomBar.setVisibility(View.INVISIBLE);
        OnStartActivity();


        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {

                if (isDone){

                    activateMainAppMenu(savedInstanceState);
                    bottomBar.setVisibility(View.VISIBLE);

                }
                return isDone;
            }
        };

        new HandlerCondition(callback,0);

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

        bottomBar.selectTabAtPosition(1);

        viewPagerMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position, true);
                //bottomBar.setInActiveTabColor(0x000033);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
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
                    case R.id.tab_user_profile:
                        viewPagerMenu.setCurrentItem(2, true);
                        break;
                }
            }

        });

    }


    protected void welcomeMessage(String username) {
        // success message
        String Msg = String.format(getResources().getString(R.string.main_app_menu_welcome_message), username);

        // results if it's successed
        Toast.makeText(getApplicationContext(), Msg,Toast.LENGTH_LONG).show();

    }


    protected abstract void OnStartActivity();


}



