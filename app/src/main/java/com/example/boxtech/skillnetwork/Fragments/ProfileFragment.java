package com.example.boxtech.skillnetwork.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.CoresAbstract.SettingsActivity;
import com.example.boxtech.skillnetwork.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;


public abstract class ProfileFragment extends Fragment {


    private final static int RESULT_LOAD_IMG = 1;
    protected App app;


    private TextView usernameProfile;
    private TextView locationTextView;
    private ImageView profileSettingsImageView;
    private TextView bioTextView;

    private CircleImageView userPictureCircleImageView;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
//    ArrayList<RecentGameModel> recentGameModels = new ArrayList<RecentGameModel>();
//    ArrayList<RecentGameModel> tmpRecentGameModels = new ArrayList<RecentGameModel>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);;
        app = App.getInstance();

        initControls(view);
        setClickableControls();
        OnStartActitvty();
        setupRecyclerView(view);

        setUserProfileInformation();

        return view;
    }


    private void initControls(View view) {
        usernameProfile = (TextView) view.findViewById(R.id.username_profile);
        bioTextView = (TextView) view.findViewById(R.id.bio_profile_textView);
        locationTextView = (TextView) view.findViewById(R.id.location_textview);
        userPictureCircleImageView = (CircleImageView) view.findViewById(R.id.user_profile_photo_circleimageview);
        profileSettingsImageView = (ImageView) view.findViewById(R.id.user_profile_settings_imageview);


//        addActivityButton = (Button) view.findViewById(R.id.add_activity_user_fragment_button);
//        noActivityMessage = (TextView) view.findViewById(R.id.no_activity_textview);
//        noActivityImageview = (ImageView) view.findViewById(R.id.no_activity_imageview);


        Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");
        Typeface playbold = Typeface.createFromAsset(getActivity().getAssets(), "playbold.ttf");
        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets(), "sansationbold.ttf");

        usernameProfile.setTypeface(playbold);
        bioTextView.setTypeface(playregular);
    }


    private synchronized void setUserProfileInformation() {
        app = App.getInstance();


        app.loadingImage(getActivity(),userPictureCircleImageView, app.getUserInformation().getPictureURL());

        usernameProfile.setText("@" + app.getUserInformation().getUsername());
        setBioTextView(app.getUserInformation().getBio());
        locationTextView.setText(app.getUserInformation().getCountry());
    }



    private void setClickableControls() {
        // these  methods used to jump to ( games , scores , following , followers ) activity .
        // I've tried the onClick(View view) method  but  it didn't work .


        userPictureCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Alpha
                if (isAllowedPremmision()) {

                    userPictureCircleImageView.setClickable(false);

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

                }

            }
        });




        profileSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);
                //Stop Handler

            }
        });

//        addActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                // Check if the user has games or not
//                if(app.getGameManager().getUserGamesNumber()<1)
//                    showNoGameDialog();
//                else{
//                    Intent i = new Intent(getContext(), NewRequestCore.class);
//                    startActivity(i);
//                    getActivity().overridePendingTransition( R.anim.slide_in_left_layout, R.anim.slide_out_left_layout);
//                    //Stop Handler
//                    handler.removeCallbacks(runnable);
//                }
//
//            }
//        });



    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recent_activities_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());

        // Add holders in reverese mode : new holders added on the top
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next hoplay)



//
//        mAdapter = createAdapter();
//
//        // Show or hide no activity elements
//        if (mAdapter.getItemCount() < 1)
//            showNoActivityElements();
//        else
//            hideNoActivityElements();




        mRecyclerView.setAdapter(mAdapter);
    }



    protected void loadPicture() {
        app.loadingImage(getActivity(),userPictureCircleImageView, app.getUserInformation().getPictureURL());

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);

                userPictureCircleImageView.setImageBitmap(imgBitmap);

                uploadPicture(userPictureCircleImageView);

            } else {

                userPictureCircleImageView.setClickable(true);

                Toast.makeText(getContext(), "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            userPictureCircleImageView.setClickable(true);
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private boolean isAllowedPremmision() {
        int permissionCheck = ContextCompat.checkSelfPermission(app.getMainAppMenuCore(),READ_EXTERNAL_STORAGE);

        if ( permissionCheck != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(app.getMainAppMenuCore(),READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            ActivityCompat.requestPermissions(app.getMainAppMenuCore(),
                    new String[]{READ_EXTERNAL_STORAGE},
                    0);

        } else return true;

        return false;
    }


//    private CommonAdapter<RecentGameModel> createAdapter() {
//        return new CommonAdapter<RecentGameModel>(recentGameModels, R.layout.new_user_activity) {
//            @Override
//            public ViewHolders OnCreateHolder(View v) {
//
//
//
//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//
//                return new ViewHolders.RecentGameHolder(v);
//            }
//
//            @Override
//            public void OnBindHolder(final ViewHolders holder, final RecentGameModel model, int position) {
//
//                // Show or hide no activity elements
//                if (mAdapter.getItemCount() < 1)
//                    showNoActivityElements();
//                else
//                    hideNoActivityElements();
//
//
//                app.loadingImage(getContext(), holder, model.getGamePhotoUrl());
//
//                Typeface playbold = Typeface.createFromAsset(getActivity().getAssets(), "playbold.ttf");
//                Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");
//
//
//
//
//                // Capitalize game name letters
//                String gameName = model.getGameName();
//                String capitlizedGameName = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);
//                if (gameName.contains(" ")) {
//                    // Capitalize game title letters
//                    String cpWord = "";
//                    for (int i = 0; i < capitlizedGameName.length(); i++) {
//                        if (capitlizedGameName.charAt(i) == 32 && capitlizedGameName.charAt(i + 1) != 32) {
//                            cpWord = capitlizedGameName.substring(i + 1, i + 2).toUpperCase() + capitlizedGameName.substring(i + 2);
//                            capitlizedGameName = capitlizedGameName.replace(capitlizedGameName.charAt(i + 1), cpWord.charAt(0));
//                        }
//                    }
//                    holder.setTitle(capitlizedGameName);
//                } else {
//                    if (capitlizedGameName.equalsIgnoreCase("cs:go")) {
//                        holder.setTitle(capitlizedGameName.toUpperCase());
//                    } else
//                        holder.setTitle(capitlizedGameName);
//                }
//
//
//
//                holder.getTitleView().setTypeface(playbold);
//                holder.setSubtitle(model.getActivityDescription());
//                holder.getSubtitleView().setTypeface(playregular);
//
//
//
//                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
//
//                new HandlerCondition(new CallbackHandlerCondition() {
//                    @Override
//                    public boolean callBack() {
//                        holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
//                        return false;
//                    }
//                }, 20000);
//
//
//                holder.getTimeView().setTypeface(playregular);
//
//
//                holder.getPicture().setBorderWidth(6);
//                // Changing title color depending on the platform
//                if (model.getGamePlatforms().equalsIgnoreCase("PC")) {
//                    holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.pc_color));
//                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.pc_color));
//                } else if (model.getGamePlatforms().equalsIgnoreCase("PS")) {
//                    holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.ps_color));
//                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.ps_color));
//                } else {
//                    holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.xbox_color));
//                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.xbox_color));
//                }
//
//
//            }
//        };
//    }



    protected void setBioTextView(String name) {
        bioTextView.setText(name);
    }

    protected void setCountryTextView(String name){
        locationTextView.setText(name);
    }

    protected abstract void OnStartActitvty();
    protected abstract void uploadPicture(CircleImageView circleImageView);
    protected abstract void logout();

}
