package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.snepos.pitchit.database.HttpHandler;
import com.snepos.pitchit.database.Request;
import com.snepos.pitchit.database.Response;

/**
 * Created by user1 on 22/08/2015.
 */
public class Comments extends ActionBarActivity {
    private static Handler mHandler;
    private int postId;
    CommentArrayAdapter commentArrayAdapter;
    ListView listView;
    String mTitle;
    ImageButton send;
    EditText newComment;
    int idea_id;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);
        commentArrayAdapter = new CommentArrayAdapter(getApplicationContext(), R.layout.comment_item);

        idea_id = getIntent().getIntExtra("idea_id", -1);
        if(idea_id == -1)
        {
            Toast.makeText(getApplicationContext(), "Error: Failed to load comments(code 1)", Toast.LENGTH_SHORT).show();
            finish();
        }

        mTitle = getString(R.string.title_section1);
        send  = (ImageButton) findViewById(R.id.send_comment_btn);
        listView = (ListView) findViewById(R.id.comments_listview);
        newComment = (EditText) findViewById(R.id.new_comment);

        Request req = new Request("get_comments", Request.App.Comments);
        req.put("idea_id", String.valueOf(idea_id));
        HttpHandler.addRequest(req);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Request req = new Request("add_comment", Request.App.Comments);
                req.put("idea_id", String.valueOf(idea_id));
                req.put("email", Login.GetUserEmail());
                req.put("text", newComment.getText().toString());
                HttpHandler.addRequest(req);

                commentArrayAdapter.add(new PitchComment(commentArrayAdapter.getCount(), Login.GetUserNickname(), newComment.getText().toString()));
                listView.setAdapter(commentArrayAdapter);

                newComment.setText("");
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case 0:
                        Toast.makeText(Comments.this, "Error: Failed to load comments(code 2)", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 1:
                        LoadComments((PitchComment[])inputMessage.obj);
                        break;
                }
            }
        };
    }

    public void LoadComments(PitchComment[] comments)
    {
        commentArrayAdapter = new CommentArrayAdapter(getApplicationContext(), R.layout.comment_item);
        for (int i = 0; i < comments.length; i++) {
            //PitchComment c = new PitchComment(i, "no name", "no comment");
            commentArrayAdapter.add(comments[i]);
        }
        listView.setAdapter(commentArrayAdapter);
    }

    public static void HandleResponse(Response response)
    {
        CommentsResponseHandler.HandleResponse(mHandler, response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.comment_menu, menu);

        /*mRefreshItem = menu.findItem(R.id.action_refresh);
        if(refreshing){
            mRefreshItem.setActionView(mRefresh);
            mRefresh.startAnimation(mRotate);
        }*/

        restoreActionBar();
        return true;

    }
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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
        }
        return false;
    }

    }
