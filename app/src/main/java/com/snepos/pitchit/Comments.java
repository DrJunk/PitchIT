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
import android.widget.TextView;
import android.widget.Toast;

import com.snepos.pitchit.database.HttpHandler;
import com.snepos.pitchit.database.Request;
import com.snepos.pitchit.database.Response;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by user1 on 22/08/2015.
 */
public class Comments extends ActionBarActivity {
    private static Handler mHandler;
    private int postId;
    Toolbar toolbar;
    CommentArrayAdapter commentArrayAdapter;
    ListView listView;
    String mTitle;
    TextView noComment;
    ImageButton send;
    EditText newComment;
    int idea_id;

    Queue<PitchComment> sentComments = new LinkedList<PitchComment>();

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.icon);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle(" PitchIt");
        setSupportActionBar(toolbar);

        noComment = (TextView)findViewById(R.id.no_comment);

        commentArrayAdapter = new CommentArrayAdapter(getApplicationContext(), R.layout.comment_item,getResources());

        idea_id = getIntent().getIntExtra("idea_id", -1);
        if(idea_id == -1)
        {
            Toast.makeText(getApplicationContext(), "Error: Failed to load comments", Toast.LENGTH_SHORT).show();
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

                PitchComment nextComment = new PitchComment(commentArrayAdapter.getCount(), Login.GetUserNickname(), newComment.getText().toString());
                sentComments.add(nextComment);
                commentArrayAdapter.add(nextComment);
                listView.setAdapter(commentArrayAdapter);

                newComment.setText("");
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case 0:
                        if(inputMessage.arg1 == 0)
                            Toast.makeText(Comments.this, "Error: Unknown error", Toast.LENGTH_SHORT).show();
                        else if(inputMessage.arg1 == 1)
                        {
                            Toast.makeText(Comments.this, "Error: Failed to load comments", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else if(inputMessage.arg1 == 2)
                        {
                            Toast.makeText(Comments.this, "Error: Failed to send comment", Toast.LENGTH_SHORT).show();
                            //commentArrayAdapter.remove(sentComments.poll());
                            sentComments.poll().alertNotSent();
                            listView.setAdapter(commentArrayAdapter);
                            System.out.println("Not sent: ");
                        }
                        break;
                    case 1:
                        LoadComments((PitchComment[])inputMessage.obj);
                        break;

                    case 2:
                        sentComments.poll();
                        break;
                }
            }
        };
    }

    public void LoadComments(PitchComment[] comments)
    {
        commentArrayAdapter.clear();
        for (int i = 0; i < comments.length; i++) {
            //PitchComment c = new PitchComment(i, "no name", "no comment");
            commentArrayAdapter.add(comments[i]);
        }
        listView.setAdapter(commentArrayAdapter);
        if(commentArrayAdapter.getCount()==0)
        {
           noComment.setVisibility(View.VISIBLE);
        }
        else
        {
            noComment.setVisibility(View.INVISIBLE);
        }

    }

    public static void HandleResponse(Response response)
    {
        CommentsResponseHandler.HandleResponse(mHandler, response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.comment_menu, menu);



        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                return true;
        }
        return false;
    }

    }
