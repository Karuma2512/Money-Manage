package com.example.ameproject;

import android.content.Context;
import android.content.SharedPreferences;

public class Auth {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Auth(Context context)
    {
        sharedPreferences = context.getSharedPreferences("login", context.MODE_PRIVATE);
        editor =  sharedPreferences.edit();
    }
    public void saveUser (int id, String name, String phone, String email, String username)
    {
        editor.putInt("id", id);
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("username", username);
        editor.apply();
    }
    public void setName (String name)
    {
        editor.putString("name", name);
        editor.apply();
    }
    public int getUserId()
    {
        return sharedPreferences.getInt("id", -1);
    }
    public String getName()
    {
        return  sharedPreferences.getString("name", null);
    }
    public String getPhone()
    {
        return sharedPreferences.getString("phone", null);
    }
    public  String getEmail()
    {
        return sharedPreferences.getString("email", null);
    }
    public String getUsername()
    {
        return sharedPreferences.getString("username", null);
    }
    public void logout()
    {
        editor.clear();
        editor.apply();
    }
}
