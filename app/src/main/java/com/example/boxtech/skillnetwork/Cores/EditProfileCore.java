package com.example.boxtech.skillnetwork.Cores;

import android.net.Uri;

import com.example.boxtech.skillnetwork.CoresAbstract.EditProfile;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;


import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileCore extends EditProfile implements FirebasePaths {

    Uri taskURI;


    @Override
    protected void OnStartActivity() {

    }

    @Override
    protected void saveProfileInfo() {

        if(taskURI !=null) {
            app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_PICTURE_URL_PATH).setValue(taskURI.toString());
        }

        if(!getBioText().equals(oldBio)) {
            app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_BIO_PATH).setValue(getBioText());
        }

        if(currentPosition !=-1)
        {
            String newValue = getCountryText();

            app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/_info_/" + FIREBASE_COUNTRY_ATTR).setValue(newValue);

        }

    }

    @Override
    protected void uploadPicture(final CircleImageView circleImageView) {
        app.uploadPicture(circleImageView,FIREBASESTORAGE_USERINFO_ATTR+"/"+app.getUserInformation().getUID()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskURI = taskSnapshot.getDownloadUrl();

                circleImageView.setClickable(true);
                setSaveButtonClickAble(true);

            }
        });
    }
}
