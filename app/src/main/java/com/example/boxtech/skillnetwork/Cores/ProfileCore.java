package com.example.boxtech.skillnetwork.Cores;


import android.util.Log;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.Auth.LoginCore;
import com.example.boxtech.skillnetwork.Fragments.ProfileFragment;
import com.example.boxtech.skillnetwork.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASESTORAGE_USERINFO_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_BIO_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_COUNTRY_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_DETAILS_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_PICTURE_URL_ATTR;
import static com.example.boxtech.skillnetwork.Interfaces.FirebasePaths.FIREBASE_PICTURE_URL_PATH;

public class ProfileCore extends ProfileFragment {

    private ChildEventListener userInformationEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            if (dataSnapshot.getKey().equals(FIREBASE_COUNTRY_ATTR) && dataSnapshot.getValue() != null) {
                String value = dataSnapshot.getValue(String.class);
                app.getUserInformation().setCountry(value);
                setCountryTextView(value);
            }



            if (dataSnapshot.getKey().equals(FIREBASE_BIO_ATTR) && dataSnapshot.getValue() != null) {
                String value = dataSnapshot.getValue(String.class);
                app.getUserInformation().setBio(value);
                setBioTextView(value);
            }

            if (dataSnapshot.getKey().equals(FIREBASE_PICTURE_URL_ATTR) && dataSnapshot.getValue() != null) {
                String value = dataSnapshot.getValue(String.class);
                app.getUserInformation().setPictureURL(value);
                loadPicture();

            }

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    protected void OnStartActitvty() {

        app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_DETAILS_ATTR).addChildEventListener(userInformationEventListener);


    }




    @Override
    protected void uploadPicture(final CircleImageView circleImageView) {
        app.uploadPicture(circleImageView,FIREBASESTORAGE_USERINFO_ATTR+"/"+app.getUserInformation().getUID()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_PICTURE_URL_PATH).setValue(taskSnapshot.getDownloadUrl().toString());
                circleImageView.setClickable(true);

            }
        });
    }

    @Override
    protected void logout() {


        App app = App.getInstance();
        app.logout(getContext());
        getActivity().overridePendingTransition(R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //userInformationRef.removeEventListener(userInformationEventListener);
    }

}
