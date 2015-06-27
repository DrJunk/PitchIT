package com.snepos.pitchit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.snepos.pitchit.sqliteHelpers.MyPrefs;

/**
 * Created by user1 on 26/06/2015.
 */
public class Login extends Activity {
    EditText password;
    EditText email;
    Button login;
    Button signUp;
    Button loginFacebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);

        password = (EditText) findViewById(R.id.login_password);
        email = (EditText) findViewById(R.id.login_email);
        login = (Button) findViewById(R.id.login_btn);
        signUp = (Button) findViewById(R.id.signUp_btn);
        loginFacebook = (Button) findViewById(R.id.login_facebook);

        signUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Intent mainIntent = new Intent(Login.this, Signup.class);
                    Login.this.startActivity(mainIntent);
                }
                return false;
            }
        });

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences(MyPrefs.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(MyPrefs.LOGIN, true);
                    editor.apply();

                    Toast.makeText(Login.this, "kafri we need a server respond", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(Login.this, MyPitch.class);
                    Login.this.startActivity(mainIntent);
                    Login.this.finish();
                }
                return false;
            }
        });

    }



    public void Connected(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(MyPrefs.FIRST_TIME, false);
        editor.apply();
    }
}
