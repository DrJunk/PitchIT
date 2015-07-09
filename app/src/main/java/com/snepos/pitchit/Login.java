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
import android.widget.TextView;
import android.widget.Toast;

import com.snepos.pitchit.sqliteHelpers.MyPrefs;

import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

/**
 * Created by user1 on 26/06/2015.
 */
public class Login extends Activity {
    EditText password;
    EditText email;
    Button login;
    Button signUp;
    Button loginFacebook;

    private GitkitClient client;

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

                    client.startSignIn();
                    /*Toast.makeText(Login.this, "kafri we need a server respond", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(Login.this, MyPitch.class);
                    Login.this.startActivity(mainIntent);
                    Login.this.finish();*/
                }
                return false;
            }
        });

        client = GitkitClient.newBuilder(this, new GitkitClient.SignInCallbacks() {
            // Implement the onSignIn method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process succeeds. A Gitkit IdToken and the signed
            // in account information are passed to the callback.
            @Override
            public void onSignIn(IdToken idToken, GitkitUser user) {
                System.out.println("Connected: " + user.getEmail());
                if(user.getIdProvider() == null) {
                    Toast.makeText(Login.this, "Please use Google or Facebook to sign in", Toast.LENGTH_LONG).show();
                    System.out.println("Not connected: " + user.getEmail());
                }

                // Now use the idToken to create a session for your user.
                // To do so, you should exchange the idToken for either a Session Token or Cookie
                // from your server.
                // Finally, save the Session Token or Cookie to maintain your user's session.
            }

            // Implement the onSignInFailed method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process fails.
            @Override
            public void onSignInFailed() {
                Toast.makeText(Login.this, "Sign in failed", Toast.LENGTH_LONG).show();
            }
        }).build();

    }



    public void Connected(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(MyPrefs.FIRST_TIME, false);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (!client.handleActivityResult(requestCode, resultCode, intent)) {
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }



    // Step 4: Override the onNewIntent method.
    // When the app is invoked with an intent, it is possible that the intent is for GitkitClient.
    // Call GitkitClient.handleIntent to check it. If the intent is for GitkitClient, the method
    // returns true to indicate the intent has been consumed.

    @Override
    protected void onNewIntent(Intent intent) {
        if (!client.handleIntent(intent)) {
            super.onNewIntent(intent);
        }
    }
}
