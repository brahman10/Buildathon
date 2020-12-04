package com.example.buildathon.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import static com.example.buildathon.Utils.Constants.IS_LOGIN;
import static com.example.buildathon.Utils.Constants.KEY_DESCRIPTION;
import static com.example.buildathon.Utils.Constants.KEY_DEVICE_ID;
import static com.example.buildathon.Utils.Constants.KEY_EMAIL;
import static com.example.buildathon.Utils.Constants.KEY_ID;
import static com.example.buildathon.Utils.Constants.KEY_LOCATION;
import static com.example.buildathon.Utils.Constants.KEY_MOBILE;
import static com.example.buildathon.Utils.Constants.KEY_NAME;
import static com.example.buildathon.Utils.Constants.KEY_PROFILE_IMG;
import static com.example.buildathon.Utils.Constants.KEY_STATUS;
import static com.example.buildathon.Utils.Constants.KEY_REWARDS;
import static com.example.buildathon.Utils.Constants.KEY_TITLE;
import static com.example.buildathon.Utils.Constants.KEY_WALLET;
import static com.example.buildathon.Utils.Constants.PREF_NAME;


/**
 * Developed by Yash Chaubey on 04,Nov,2020
 */
public class SessionManagment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;

    public SessionManagment(Context context) {
        this.context = context;
        pref=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

    public void createLoginSession(String id, String name, String mobile, String img , String email,String status,String title, String location, String description)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_ID,id);
        editor.putString(KEY_MOBILE,mobile);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_PROFILE_IMG,img);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_STATUS,status);
        editor.putString(KEY_TITLE,title);
        editor.putString(KEY_DESCRIPTION,description);
        editor.putString(KEY_LOCATION,location);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_PROFILE_IMG, pref.getString(KEY_PROFILE_IMG, null));
        user.put(KEY_WALLET, pref.getString(KEY_WALLET, null));
        user.put(KEY_REWARDS, pref.getString(KEY_REWARDS, null));
        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));
        user.put(KEY_TITLE, pref.getString(KEY_TITLE, null));
        user.put(KEY_DESCRIPTION, pref.getString(KEY_DESCRIPTION, null));
        user.put(KEY_LOCATION, pref.getString(KEY_LOCATION, null));
        user.put(KEY_DEVICE_ID,pref.getString(KEY_DEVICE_ID,null));
        return user;
    }

    public boolean isLogin()
    { return pref.getBoolean(IS_LOGIN,false);  }

    public void logoutSession(){
        editor.clear();
        editor.commit();
    }

    public void updateProfile(String name, String email, String img,String status,String title, String location, String description)
    {
       editor.putString(KEY_NAME,name);
       editor.putString(KEY_EMAIL,email);
       editor.putString(KEY_PROFILE_IMG,img);
       editor.putString(KEY_STATUS,status);
        editor.putString(KEY_TITLE,title);
        editor.putString(KEY_DESCRIPTION,description);
        editor.putString(KEY_LOCATION,location);
       editor.apply();
    }

    /*public void updateWallet(String wallet)
    {
        editor.putString(KEY_WALLET,wallet);
        editor.apply();
    }
    public void updateQuizStatus(String quiz_status)
    {
        editor.putString(KEY_QUIZ_STATUS,quiz_status);
        editor.apply();
    }
    public void updateRewards(String rew)
    {
        editor.putString(KEY_REWARDS,rew);
        editor.apply();
    }*/

}
