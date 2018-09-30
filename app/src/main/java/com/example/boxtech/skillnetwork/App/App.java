package com.example.boxtech.skillnetwork.App;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.boxtech.skillnetwork.Adapters.ViewHolders;
import com.example.boxtech.skillnetwork.Cores.Auth.LoginCore;
import com.example.boxtech.skillnetwork.Cores.MainMenuCore;
import com.example.boxtech.skillnetwork.Interfaces.FirebasePaths;
import com.example.boxtech.skillnetwork.Models.RequestModel;
import com.example.boxtech.skillnetwork.Models.SkillsModel;
import com.example.boxtech.skillnetwork.Models.UserInformation;
import com.example.boxtech.skillnetwork.R;
import com.example.boxtech.skillnetwork.Services.TimeStamp;
import com.example.boxtech.skillnetwork.untill.SkillManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class App extends Application  implements FirebasePaths{

    public static boolean isWelcomed = false;
    private static App instance;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseUserNames;
    private DatabaseReference databaseUsersInfo;
    private DatabaseReference databaseChat;
    private DatabaseReference databaseChatPrivate;
    private DatabaseReference databaseChatPending;
    private DatabaseReference databaseBaseRequests;
    private DatabaseReference databaseReqeustCollections;
    private DatabaseReference databaseRequestCategories;
    private DatabaseReference databaseSkills;
    private FirebaseStorage storage;

    private UserInformation userInformation;
    private SkillManager skillManager;


    private FirebaseAuth mAuth;  // firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private TimeStamp timeStamp;

   private ArrayAdapter<String> skillAdapter;
   private HashMap<String,RequestModel> requests = new HashMap<>();
   private String userToken = "";


   private RequestModel passRequest;



    private MainMenuCore mainAppMenuCore;




    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseUserNames = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_USER_NAMES_ATTR);
        databaseUsersInfo = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_USERS_INFO_ATTR);
        databaseChat = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_CHAT_ATTR);
        databaseChatPrivate = firebaseDatabase.getReferenceFromUrl(FB_PRIVATE_CHAT_PATH);
        databaseChatPending = databaseChat.child(FIREBASE_PENDING_CHAT_ATTR);

        databaseSkills = firebaseDatabase.getReference().child("Skills");
        databaseBaseRequests = firebaseDatabase.getReference().child("Requests");
        databaseRequestCategories = databaseBaseRequests.child("Categories");
        databaseReqeustCollections = databaseBaseRequests.child("Collections");
        storage = FirebaseStorage.getInstance();


        userInformation = new UserInformation();
        skillManager =new SkillManager();
        timeStamp = new TimeStamp();



        // gameAdapter for search request
        ArrayList<String> list = new ArrayList<String>();
        skillAdapter = new ArrayAdapter<String>(this,
                R.layout.auto_complete_text_view_list_item, list);


    }

    //
    // this method return a instance of this app class
    public static synchronized App getInstance() {

        if(instance == null)
            instance = new App();

        return instance;
    }



    // this method return firebase auth
    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public DatabaseReference getDatabaseUserNames() {
        return databaseUserNames;
    }

    public DatabaseReference getDatabaseUsersInfo() {
        return databaseUsersInfo;
    }

    public DatabaseReference getDatabaseChat() {
        return databaseChat;
    }

    public DatabaseReference getDatabaseChatPrivate() {
        return databaseChatPrivate;
    }

    public DatabaseReference getDatabaseChatPending() {
        return databaseChatPending;
    }

    public DatabaseReference getDatabaseReqeustCollections() {
        return databaseReqeustCollections;
    }

    public DatabaseReference getDatabaseRequestCategories() {
        return databaseRequestCategories;
    }

    public DatabaseReference getDatabaseRequests() {
        return databaseBaseRequests;
    }


    public DatabaseReference getDatabaseSkills() {
        return databaseSkills;
    }

    public void sendEmailVerification(FirebaseUser user, final String username)
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Verificetion sent to your email",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public String convertFromTimeStampToTimeAgo(long timeStamp) {
        return TimeStamp.getTimeAgoFromTimestamp(timeStamp);
    }

    public String convertFromTimeStampToDate(long timeStamp)
    {
        DateFormat sdf = new SimpleDateFormat("K:mm a");
        Date netDate = (new Date(Long.parseLong(String.valueOf(timeStamp))));
        String time = sdf.format(netDate);
        return time;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }



    public void loadingImage(Context c, CircleImageView circleImageView, String pictureURL) {


        if (pictureURL == null || pictureURL.isEmpty() || pictureURL.equals("\\s++"))
            return;

        Picasso.with(c)
                .load(pictureURL)
                .error(R.drawable.profile_default_photo)
                .into(circleImageView);


    }


    public UploadTask uploadPicture(CircleImageView circleImageView, String uid) {

        circleImageView.setDrawingCacheEnabled(true);
        circleImageView.buildDrawingCache();
        Bitmap bitmap = circleImageView.getDrawingCache();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] picData = byteArrayOutputStream.toByteArray();

        StorageReference storageReference = storage.getReference();
        StorageReference picRef = storageReference.child(uid + "/profile_picture.png");

        UploadTask uploadTask = picRef.putBytes(picData);
        return uploadTask;
    }



    public void addSkill(String id,String name,String type)
    {
        SkillsModel skillsModel = new SkillsModel();
        skillsModel.setSkillsId(id);
        skillsModel.setSkillName(name);
        skillsModel.setSkillType(type);

        skillManager.addSkill(skillsModel);
        skillAdapter.add(name);
        skillAdapter.notifyDataSetChanged();
    }


    public void reloadSkills()
    {
        skillAdapter.clear();

        for(SkillsModel skillsModel : getSkillManager().getAllSkills()){
            skillAdapter.add(skillsModel.getSkillName());
        }

        skillAdapter.notifyDataSetChanged();
    }







    public void clearAdapter()
    {
        skillAdapter.clear();
    }

    public void addRequest(RequestModel requestModel)
    {
        requests.put(requestModel.getRequestId(),requestModel);
    }

    public RequestModel getRequestById(String id)
    {

        Log.i("--->",requests.containsKey(id) + "");

        if(requests.containsKey(id))
        {
            return requests.get(id);
        }

        return null;
    }





    public   ArrayList<String> getCountry()
    {
        ArrayList<String> countryTypesArray = new ArrayList<>();
        String[] locales = Locale.getISOCountries();


        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countryTypesArray.add(obj.getDisplayCountry());

        }

        return countryTypesArray;
    }


    public void logout(final Context c) {


        App app = App.getInstance();
        mAuth.signOut();


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                LoginCore.restartApp(c);
            }
        });


        Toast.makeText(c,R.string.settings_screen_logout_message,Toast.LENGTH_LONG).show();

    }

    public SkillManager getSkillManager() {
        return skillManager;
    }



    public ArrayAdapter<String> getSkillAdapter() {
        return skillAdapter;
    }

    public RequestModel getPassRequest() {
        return passRequest;
    }

    public void setPassRequest(RequestModel passRequest) {
        this.passRequest = passRequest;
    }

    public void cleanPassRequest()
    {
        passRequest = null;
    }


    public void setMainAppMenuCore(MainMenuCore mainAppMenuCore) {
        this.mainAppMenuCore = mainAppMenuCore;
    }

    public MainMenuCore getMainAppMenuCore() {
        return mainAppMenuCore;
    }


}
