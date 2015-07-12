package com.snepos.pitchit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.HttpHandler;
import com.snepos.pitchit.database.Request;
import com.snepos.pitchit.database.Response;
import com.snepos.pitchit.sqliteHelpers.MyPrefs;

import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

/**
 * Created by user1 on 26/06/2015.
 */
public class Login extends Activity {
    private static Handler mHandler;
    private static String userEmail = MyPrefs.NOT_CONNECTED;
    private static String userNickname = "";

    Button signIn;

    private GitkitClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);

        signIn = (Button) findViewById(R.id.signIn_btn);

        signIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //Intent mainIntent = new Intent(Login.this, Signup.class);
                    //Login.this.startActivity(mainIntent);
                    client.startSignIn();
                }
                return false;
            }
        });

        if(getIntent().getBooleanExtra("JustRegistered", false))
        {
            Request req = new Request("login", Request.App.Login);
            req.put("email", userEmail);
            req.put("key", KeyGenerator.GenerateKey(userEmail));
            HttpHandler.addRequest(req);

            signIn.setEnabled(false);
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {

                switch (inputMessage.what) {
                    case 0: // Failed
                        Toast.makeText(Login.this, "Error: Login failed", Toast.LENGTH_SHORT).show();
                        signIn.setEnabled(true);
                        break;
                    case 1: // Succeeded
                        String loginKey = KeyGenerator.GenerateKey(userEmail);
                        SharedPreferences settings = getApplicationContext().getSharedPreferences(MyPrefs.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(MyPrefs.LOGIN, true);
                        editor.putString(MyPrefs.EMAIL, userEmail);
                        editor.putString(MyPrefs.LOGINKEY, loginKey);
                        editor.apply();

                        if(getIntent().getBooleanExtra("JustRegistered", false))
                        {
                            Intent mainIntent = new Intent(Login.this,TutorialSwipe.class);
                            startActivity(mainIntent);
                            finish();
                        }
                        else {
                            Intent mainIntent = new Intent(Login.this, MyPitch.class);
                            startActivity(mainIntent);
                            finish();
                        }
                        break;
                    case 2: // Not registered
                        Intent registerIntent = new Intent(Login.this, Signup.class);
                        startActivity(registerIntent);
                        finish();
                        break;
                }
            }
        };

        /*
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
                    Login.this.finish();  * /
                }
                return false;
            }
        });*/

        client = GitkitClient.newBuilder(this, new GitkitClient.SignInCallbacks() {
            // Implement the onSignIn method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process succeeds. A Gitkit IdToken and the signed
            // in account information are passed to the callback.
            @Override
            public void onSignIn(IdToken idToken, GitkitUser user) {
                if(user.getIdProvider() == null) {
                    Toast.makeText(Login.this, "Please use Google or Facebook to sign in", Toast.LENGTH_LONG).show();
                }
                else
                {
                    userEmail = user.getEmail();

                    Request req = new Request("login", Request.App.Login);
                    req.put("email", userEmail);
                    req.put("key", KeyGenerator.GenerateKey(userEmail));
                    HttpHandler.addRequest(req);

                    signIn.setEnabled(false);
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

    public static void LogOut(Context context)
    {
        userEmail = MyPrefs.NOT_CONNECTED;
        SharedPreferences settings = context.getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MyPrefs.EMAIL, userEmail);
        editor.apply();
    }

    public static boolean LoadUserEmail(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        userEmail = settings.getString(MyPrefs.EMAIL, MyPrefs.NOT_CONNECTED);
        String loginKey = settings.getString(MyPrefs.LOGINKEY, "");
        if(!KeyGenerator.CheckKey(userEmail, loginKey))
        {
            userEmail = MyPrefs.NOT_CONNECTED;
        }
        return userEmail != MyPrefs.NOT_CONNECTED;
    }

    public static String GetUserEmail()
    {
        return userEmail;
    }

    public static String GetUserNickname()
    {
        return userNickname;
    }

    public static void HandleResponse(Response response)
    {
        LoginResponseHandler.HandleResponse(mHandler, response);
    }
}
