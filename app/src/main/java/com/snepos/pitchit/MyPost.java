package com.snepos.pitchit;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

    EditText body;
    EditText head;
    TextView bodyLeftLength;
    private Menu _menu = null;

    MyTimerTask task;
    final long seconds = 3;
    Timer timer;

    private boolean waitingForResponse;

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

        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.my_pitch, menu);
        //getMenuInflater().inflate(R.menu.my_pitch, menu);
        //restoreActionBar();
        head = (EditText) findViewById(R.id.new_Head);
        body = (EditText) findViewById(R.id.new_Body);

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

