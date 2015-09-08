package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.HttpHandler;
import com.snepos.pitchit.database.IdeaData;
import com.snepos.pitchit.database.Request;
import com.snepos.pitchit.database.Response;
import com.snepos.pitchit.sqliteHelpers.DatabaseHandler;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MyPost extends ActionBarActivity {
    private static Handler mHandler;
    Toolbar toolbar;
    EditText body;
    EditText head;
    TextView publisher;
    TextView bodyLeftLength;
    LinearLayout top;
    private Menu _menu = null;
    int sum;
    MyTimerTask task;
    final long seconds = 3;
    Timer timer;

    private boolean waitingForResponse;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        waitingForResponse = false;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                waitingForResponse = false;
                switch (inputMessage.what) {
                    case 0:
                        Toast.makeText(MyPost.this, "Error: Failed to send idea to server", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Database.PostRefreshNews();
                        Toast.makeText(MyPost.this, "Sent", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        };

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.icon);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle(" PitchIt");
        setSupportActionBar(toolbar);
        head = (EditText) findViewById(R.id.new_Head);
        body = (EditText) findViewById(R.id.new_Body);
        publisher = (TextView) findViewById(R.id.new_publisher);
        publisher.setText(Login.GetUserNickname().toString());
        top = (LinearLayout) findViewById(R.id.top_layout);

        sum = 5000;
        for (int i=0; i< publisher.length(); i++)
        {
            sum+= publisher.getText().toString().charAt(i);
        }
        sum *= sum;
        final Integer temp = CardArrayAdapter.matColors.get(sum % CardArrayAdapter.matColors.size());
        top.setBackgroundColor(new Integer(temp));

        bodyLeftLength = (TextView) findViewById(R.id.body_chars_left);
        task = new MyTimerTask();
        timer = new Timer();
        timer.schedule(task, seconds * 1000L);

        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bodyLeftLength.setText("(chars left: " + (200 - body.getText().length()) + ")");
                //bodyCharsLeftTime.postDelayed(runnable, 3000);
                task.cancel();
                task = new MyTimerTask();
                timer.schedule(task, seconds * 1000L);


                //bodyLeftLength.setText("");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        body.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //body.setText(body.getText()+"\n");
                    // Toast.makeText(MyPost.this, body.getText(), Toast.LENGTH_SHORT).show();
                    return false;
                }
                return false;
            }
        });

    }

    public void setCharViewers()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

             bodyLeftLength.setText("");
            }
        });

    }

    public static void HandleResponse(Response response)
    {
        MyPostResponseHandler.HandleResponse(mHandler, response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ///Inflate the menu; //this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.login, menu);
        getMenuInflater().inflate(R.menu.new_post, menu);
        _menu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                return true;
            case R.id.action_send:
                //here here here here send
                if(!waitingForResponse) {
                    if(head.getText().length() < 4 || body.getText().length() < 4)
                    {
                        Toast.makeText(MyPost.this, "Head and body length must be at least 4", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    Request req = new Request("add_idea", Request.App.MyPost);
                    req.put("title", head.getText().toString());
                    req.put("text", body.getText().toString());
                    req.put("email", Login.GetUserEmail());
                    HttpHandler.addRequest(req);

                    Toast.makeText(MyPost.this, "Sending...", Toast.LENGTH_SHORT).show();

                    waitingForResponse = true;
                }
/*
                //tom tries to figure it out*****************************************************************
                DatabaseHandler db = new DatabaseHandler(this);
                Random rand = new Random();
                Card post = new Card(rand.nextInt(),head.getText().toString(),body.getText().toString(),false,0,false,false,0,0);
                db.addPost(post, DatabaseHandler.Table.NEW);
                //tom tries figured it out*******************************************************************
*/
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            setCharViewers();
            task.cancel();
        }
    }
}

