package com.folkus.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.folkus.data.remote.response.LoginData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class UserPreferences {
    private SharedPreferences sharedPreferences;
    private final String KEY_USER_ID = "PREF_KEY_USER_ID";
    private final String KEY_USER_NAME = "PREF_KEY_USER_NAME";
    private final String KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL";
    private final String KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private final String KEY_PROFILE_DETAILS = "PREF_KEY_PROFILE_DETAILS";

    public UserPreferences(Context context) {
        sharedPreferences = (SharedPreferences) context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
    }

    public void setStringData(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key, String defaultValue) {
        String value = sharedPreferences.getString(key, defaultValue);
        return value;
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, 0);
    }
    public void setUserId(int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }
    public void removeUserId(){
        sharedPreferences.edit().remove(KEY_USER_ID).apply();
    }


    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }
    public void removeUserName(){
        sharedPreferences.edit().remove(KEY_USER_NAME).apply();
    }


    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }
    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.apply();
    }
    public void removeUserEmail(){
        sharedPreferences.edit().remove(KEY_USER_EMAIL).apply();
    }


    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }
    public void setAccessToken(String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }
    public void removeAccessToken(){
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply();
    }

    public LoginData getProfileDetails() {
        String profileData = sharedPreferences.getString(KEY_PROFILE_DETAILS, "");
        if (profileData != null) {
            Gson gson = new Gson();
            LoginData myPojo;
            Type collectionType = new TypeToken<LoginData>() {}.getType();
            myPojo = gson.fromJson(profileData, collectionType);
            return myPojo;
        }
        return null;
    }
    public void setProfileDetails(LoginData loginData) {
        String profileData = new Gson().toJson(loginData);
        sharedPreferences.edit().putString(KEY_PROFILE_DETAILS, profileData).apply();
    }
    public void removeProfileDetails(){
        sharedPreferences.edit().remove(KEY_PROFILE_DETAILS).apply();
    }
}
