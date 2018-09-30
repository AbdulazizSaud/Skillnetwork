package com.example.boxtech.skillnetwork.CoresAbstract;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.boxtech.skillnetwork.Adapters.SpinnerAdapter;
import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public abstract class EditProfile extends AppCompatActivity {

    private final  int RESULT_LOAD_IMG=1;

    private TextView changeUserPhotoTextview , activityTitle;
    private EditText userBioEdittext , gameProviderEdittext , emailEdittext;
    private MaterialBetterSpinner gamesProvidersSpinner;
    private ImageView closeButton;
    private Button saveButton;
    private CircleImageView userPictureCircleImageView;

    private RelativeLayout activityLayout ;

    private MaterialBetterSpinner countrySpinner;
    protected ArrayAdapter countryTypes;

    protected App app;

    protected String oldBio;

    protected int currentPosition =-1;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        app = App.getInstance();

        currentPosition=-1;

        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        final Typeface sansation = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");

        activityLayout = (RelativeLayout) findViewById(R.id.edit_profile_relativelayout);

        activityTitle = (TextView) findViewById(R.id.edit_profile_title_textview) ;
        changeUserPhotoTextview = (TextView) findViewById(R.id.change_profile_photo_textview);
        userBioEdittext = (EditText) findViewById(R.id.edit_user_bio_edittext);

        closeButton = (ImageView) findViewById(R.id.go_back_to_preferences_from_edit_profile);
        saveButton = (Button) findViewById(R.id.update_edit_profile_button);
        userPictureCircleImageView = (CircleImageView) findViewById(R.id.user_profile_photo_circleimageview);



        final ArrayList<String> countryTypesArray = app.getCountry();
        countrySpinner = (MaterialBetterSpinner) findViewById(R.id.edit_profile_country_spinner);
        countryTypes = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,countryTypesArray);

        countryTypes.notifyDataSetChanged();
        countrySpinner.setAdapter(countryTypes);


        // Initialize email edittext
        emailEdittext = (EditText) findViewById(R.id.edit_profile_email_edittext);
        emailEdittext.setTypeface(playregular);
        emailEdittext.setText(app.getUserInformation().getUserEmail());
        emailEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(activityLayout, R.string.edit_profile_change_email_message, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        });


        changeUserPhotoTextview.setTypeface(playbold);
        userBioEdittext.setTypeface(playregular);
        saveButton.setTypeface(playregular);
        activityTitle.setTypeface(sansation);
        countrySpinner.setTypeface(playregular);




        userPictureCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                // Alpha
                                if (isAllowedPremmision()) {

                                    userPictureCircleImageView.setClickable(false);
                                    setSaveButtonClickAble(false);

                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    // Start the Intent
                                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                                }

            }
        });


        changeUserPhotoTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isAllowedPremmision()) {

                    userPictureCircleImageView.setClickable(false);
                    setSaveButtonClickAble(false);

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                }

            }
        });





        countrySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
            }
        });



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileInfo();
                Toast.makeText(getApplicationContext(),R.string.edit_profile_success_message, Toast.LENGTH_LONG).show();
                finish();
            }
        });

        setUserProfileInformation();
        OnStartActivity();





    }

    private boolean isAllowedPremmision() {
        int permissionCheck = ContextCompat.checkSelfPermission(EditProfile.this,READ_EXTERNAL_STORAGE);

        if ( permissionCheck != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this,READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            ActivityCompat.requestPermissions(EditProfile.this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    0);

        } else return true;

        return false;
    }



    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }



    private synchronized void setUserProfileInformation() {

        oldBio = app.getUserInformation().getBio();

        userBioEdittext.setText(app.getUserInformation().getBio());
        app.loadingImage(getApplication(),userPictureCircleImageView,app.getUserInformation().getPictureURL());


    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);

                userPictureCircleImageView.setImageBitmap(imgBitmap);

                uploadPicture(userPictureCircleImageView);

            } else {

                userPictureCircleImageView.setClickable(true);
                setSaveButtonClickAble(true);

                Toast.makeText(getApplicationContext(), "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            userPictureCircleImageView.setClickable(true);
            setSaveButtonClickAble(true);

            Toast.makeText(getApplicationContext(), "Something went wrong" , Toast.LENGTH_LONG)
                    .show();
            Log.e("--->",e.getLocalizedMessage() );
        }

    }


    public void slideInFromLeft(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        animation.setDuration(200);
        viewToAnimate.startAnimation(animation);
    }

    public void slideOutToRight(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
    }


    protected String getBioText()
    {
        return userBioEdittext.getText().toString().trim();
    }

    protected String getCountryText(){
        return countrySpinner.getText().toString().trim();
    }

    protected void setSaveButtonClickAble(boolean b)
    {
        saveButton.setClickable(b);
    }

    protected abstract void OnStartActivity();
    protected abstract void saveProfileInfo();
    protected abstract void uploadPicture(CircleImageView circleImageView);
}
