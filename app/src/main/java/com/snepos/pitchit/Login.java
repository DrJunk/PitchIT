package com.snepos.pitchit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.snepos.pitchit.sqliteHelpers.MyPrefs;

/**
 * Created by user1 on 26/06/2015.
 */
public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }



    public void Connected(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(MyPrefs.FIRST_TIME, false);
        editor.apply();
    }
}
