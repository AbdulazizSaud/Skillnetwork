package com.example.boxtech.skillnetwork.Interfaces;


public interface FirebasePaths {

    public static final String FIREBASE_PRIVATE_ATTR = "_private_";


    public static final String FIREBASE_FRIENDS_LIST_ATTR = "_freelancers_list_";
    public static final String FIREBASE_DETAILS_ATTR = "_info_";
    public static final String FIREBASE_USER_NAMES_ATTR = "_user_names_";
    public static final String FIREBASE_USERS_INFO_ATTR = "_users_info_";
    public static final String FIREBASE_USERS_LIST_ATTR = "_users_";
    public static final String FIREBASE_REQUEST_TIME_STAMP_ATTR = "timeStamp";





    public static final String FIREBASE_USER_CHAT_REFERENCES = "_chat_refs_";
    public static final String FIREBASE_CHAT_ATTR = "Chat";
    public static final String FIREBASE_PENDING_CHAT_ATTR = "_pending_chat_";
    public static final String FIREBASE_CHAT_MESSAGES = "_messages_";
    public static final String FIREBASE_USER_PRIVATE_CHAT = FIREBASE_USER_CHAT_REFERENCES+"/"+FIREBASE_PRIVATE_ATTR;
    public static final String FIREBASE_CHAT_USERS_LIST_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_USERS_LIST_ATTR;

    public static final String FIREBASE_COUNTER_PATH = "_counter_";
    public static final String FIREBASE_OPPONENT_ID_ATTR = "_opponent_ID_";


    // ROOT PATH

    public static final String FB_ROOT = "https://skillnetwork-cbdf0.firebaseio.com/";
    public static final String FB_USERS_PATH = FB_ROOT+FIREBASE_USERS_INFO_ATTR+"/";
    public static final String FB_CHAT_PATH = FB_ROOT+FIREBASE_CHAT_ATTR+"/";
    public static final String FB_PRIVATE_CHAT_PATH = FB_CHAT_PATH+FIREBASE_PRIVATE_ATTR+"/";

    // Requests
    public static final String FB_REQUESTS_REFERENCE = "_requests_";

    // Regions
    public static final String FB_REGIONS_REFERENCE = "_regions_" ;



    // Support
    public static final String FIREBASE_SUPPORT_REFERENCE = "_support_";
    public static final String FIREBASE_SUPPORT_TIME_ATTR = "timestamp";
    public static  final String FIREBASE_SUPPORT_TITLE_ATTR = "title";
    public static final String FIREBASE_SUPPORT_MESSAGE_ATTR = "message";
    public static  final String FIREBASE_SUPPORT_EMAIL_ATTR = "email";
    public static  final String FIREBASE_SUPPORT_USERNAME = "username";
    public static final  String FIREBASE_SUPPORT_UID = "UID";

    // USER INFO

    public static final String FIREBASE_USERNAME_ATTR = "username";
    public static final String FIREBASE_BIO_ATTR = "bio";
    public static final String FIREBASE_ACCOUNT_TYPE_ATTR = "acc_type";
    public static final String FIREBASE_PICTURE_URL_ATTR =  "pictureURL";
    public static final String FIREBASE_EMAIL_ATTR = "userEmail";
    public static final String FIREBASE_COUNTRY_ATTR = "country";
    public static final String FIREBASE_TYPE_ATTR = "type";
    public static final String FIREBASE_USER_REQUESTS_ATTR = "_requests_refs_";
    public static final String FIREBASE_USER_PENDING_REQUESTS_ATTR = "_pending_refs_";




    public static final String FIREBASE_USERNAME_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_USERNAME_ATTR;

    public static final String FIREBASE_BIO_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_BIO_ATTR;
    public static final String FIREBASE_ACCOUNT_TYPE_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_ACCOUNT_TYPE_ATTR;

    public static final String FIREBASE_PICTURE_URL_PATH =  FIREBASE_DETAILS_ATTR+"/"+FIREBASE_PICTURE_URL_ATTR;
    public static final String FIREBASE_EMAIL_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_EMAIL_ATTR;

    public static final String FIREBASESTORAGE_USERINFO_ATTR ="UsersInfo";

    public static  final String FIREBASE_USERS_TOKENS_ATTR = "_users_tokens_";

}
