package com.example.boxtech.skillnetwork.CoresAbstract;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.Cores.AddSkillCore;
import com.example.boxtech.skillnetwork.Cores.Auth.LoginCore;
import com.example.boxtech.skillnetwork.Cores.EditProfileCore;
import com.example.boxtech.skillnetwork.Interfaces.Constants;
import com.example.boxtech.skillnetwork.R;
import com.example.boxtech.skillnetwork.Services.SettingsScreen;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class SettingsActivity extends PreferenceActivity {


    private Context context;
    private FirebaseAuth mAuth;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);

        app = App.getInstance();
        mAuth = app.getAuth();

        setupPreferences();
    }

    public void setupPreferences()
    {
        Preference logoutPref = (Preference) findPreference("settings_logout");
        logoutPrefSetup(logoutPref);
        editProfilePrefSetup();
        EditSkillsPrefSetup();
        termPrefSetup();
        aboutSetupPref();
        changePasswordSetupPref();


    }

    private void changePasswordSetupPref() {
        // Change password preference
        Preference changePasswordPref = (Preference) findPreference("settings_change_password");
        changePasswordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
//
//               Intent i = new Intent(context, ChangePasswordCore.class);
//               startActivity(i);
                return true;
            }
        });
    }

    private void aboutSetupPref() {
        // About Preference
        Preference aboutPref = (Preference) findPreference("settings_version");
        aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                final Dialog aboutDialog;
                aboutDialog = new Dialog(getApplicationContext());
                aboutDialog.setContentView(R.layout.about_dialog);
                aboutDialog.show();

                aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView appName , appVersion , appCopyright;
                Button okButton;


                appName = (TextView) aboutDialog.findViewById(R.id.app_name_about_dialog);
                appVersion = (TextView) aboutDialog.findViewById(R.id.app_version_about_dialog);
                appCopyright = (TextView) aboutDialog.findViewById(R.id.app_copyright_about_dialog);

                okButton = (Button) aboutDialog.findViewById(R.id.ok_button_about_dialog);

                Typeface playregular = Typeface.createFromAsset(getResources().getAssets() ,"playregular.ttf");
                Typeface playbold = Typeface.createFromAsset(getResources().getAssets() ,"playbold.ttf");
                Typeface sansationbold = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");

                appName.setTypeface(playbold);
                appVersion.setTypeface(playregular);
                appCopyright.setTypeface(playregular);
                okButton.setTypeface(sansationbold);

                // Dismis dialog
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aboutDialog.dismiss();
                    }
                });





                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = aboutDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                return true;
            }
        });
    }

    private void editProfilePrefSetup() {
        // go to edit profile activity
        Preference editProfilePref = (Preference) findPreference("settings_edit_profile");
        editProfilePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getApplicationContext(), EditProfileCore.class);
                startActivity(i);
                return true;
            }
        });
    }

    private void termPrefSetup() {
        Preference termsAndConditions = (Preference) findPreference("settings_terms");
        termsAndConditions.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });
    }

    private void EditSkillsPrefSetup() {
        final Preference settingsEditSkillPref = (Preference) findPreference("settings_edit_skill");

        if(app.getUserInformation().getType().equals(Constants.ACCOUNT_TYPE_CUSTOMER))
            getPreferenceScreen().removePreference(settingsEditSkillPref);


        settingsEditSkillPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(app.getUserInformation().getType().equals(Constants.ACCOUNT_TYPE_FREELANCER))
                {
                    Intent i = new Intent(getApplicationContext(), AddSkillCore.class);
                    i.putExtra("CameFrom", "Settings");
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                   overridePendingTransition(R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);
                } else {
                    getPreferenceScreen().removePreference(settingsEditSkillPref);
                }

                return true;
            }
        });
    }

    private void logoutPrefSetup(Preference logoutPref) {
        logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Do whatever you want here


                app.logout(getApplication());
                // To delete cache
                try {
                    File dir = context.getCacheDir();
                    deleteDir(dir);
                } catch (Exception e) {}


                Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        LoginCore.restartApp(context);
                    }
                });

                Toast.makeText(getApplicationContext(), R.string.settings_screen_logout_message, Toast.LENGTH_LONG).show();


                // Way 1
                try {
                    // clearing app data
                    String packageName = context.getPackageName();
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("pm clear "+packageName);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
