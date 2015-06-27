package com.snepos.pitchit;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by user1 on 27/06/2015.
 */
public class Signup extends Activity {

    EditText userName;
    EditText password;
    EditText password_confirm;
    EditText email;
    Button signUp;
    ImageView checkConfirm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);

        password = (EditText) findViewById(R.id.sign_up_password);
        password_confirm = (EditText) findViewById(R.id.sign_up_password_confirmed);
        userName = (EditText) findViewById(R.id.sign_up_username);
        email = (EditText) findViewById(R.id.sign_up_email);
        signUp = (Button) findViewById(R.id.signUp_server);

        checkConfirm = (ImageView) findViewById(R.id.smallvconfirm);
        checkConfirm.setVisibility(View.INVISIBLE);

        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password_confirm.getText().toString().equals(password.getText().toString()))
                    checkConfirm.setVisibility(View.VISIBLE);
                else
                    checkConfirm.setVisibility(View.INVISIBLE);

            }
        });
        password_confirm.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if(password_confirm.getText().toString().equals(password.getText().toString()))
                    checkConfirm.setVisibility(View.VISIBLE);
                else
                    checkConfirm.setVisibility(View.INVISIBLE);

            }
        });

        signUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(Signup.this, "kafri we need a server respond", Toast.LENGTH_SHORT).show();
                    Signup.this.finish();
                }
                return false;
            }
        });

    }
}
