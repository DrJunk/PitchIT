package com.snepos.pitchit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.snepos.pitchit.database.HttpHandler;
import com.snepos.pitchit.database.Request;
import com.snepos.pitchit.database.Response;

/**
 * Created by user1 on 27/06/2015.
 */
public class Signup extends Activity {
    private static Handler mHandler;

    EditText fieldNickname;
    Button btnSignUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);

        fieldNickname = (EditText) findViewById(R.id.sign_up_nickname);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);

        btnSignUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    String nickname = fieldNickname.getText().toString();
                    String userEmail = Login.GetUserEmail();
                    if(nickname.length() > 4)
                    {
                        Request req = new Request("register", Request.App.Register);
                        req.put("email", userEmail);
                        req.put("nick_name", nickname);
                        req.put("key", KeyGenerator.GenerateKey(userEmail + nickname));
                        HttpHandler.addRequest(req);
                    }
                    else
                        Toast.makeText(Signup.this, "Nickname's length must be above 4.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                Intent loginIntent;
                switch (inputMessage.what) {
                    case 0: // Failed
                        Toast.makeText(Signup.this, "Error.", Toast.LENGTH_SHORT).show();
                        loginIntent = new Intent(Signup.this, Login.class);
                        Signup.this.startActivity(loginIntent);
                        Signup.this.finish();
                        break;
                    case 1: // Succeeded

                        Request req = new Request("login", Request.App.Register);
                        req.put("email", Login.GetUserEmail());
                        req.put("key", KeyGenerator.GenerateKey(Login.GetUserEmail()));
                        HttpHandler.addRequest(req);
                        loginIntent = new Intent(Signup.this, Login.class);
                        loginIntent.putExtra("JustRegistered", true);
                        Signup.this.startActivity(loginIntent);
                        Signup.this.finish();
                        break;
                }
            }
        };
    }

    public static void HandleResponse(Response response)
    {
        SignupResponseHandler.HandleResponse(mHandler, response);
    }
}
