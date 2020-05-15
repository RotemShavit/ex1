package com.example.ex1;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Todoboom extends Application
{
    String TAG = "Application";
    @Override
    public void onCreate(){
        super.onCreate();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String s = sp.getString("todo_list", null);
        if(s != null)
        {
            String[] cur_s = s.split(";");
            Log.d(TAG, "" + (cur_s.length/2)); // The list is twice bigger - 2 cells per each TODO_item
        }
    }
}
