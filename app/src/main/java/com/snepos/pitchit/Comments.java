package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

/**
 * Created by user1 on 22/08/2015.
 */
public class Comments extends ActionBarActivity {
    private static Handler mHandler;
    private int postId;
    CommentArrayAdapter commentArrayAdapter;
    ListView listView;
    String mTitle ;
    ImageButton send;
    EditText newComment;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);
        mTitle = getString(R.string.title_section1);
        send  = (ImageButton) findViewById(R.id.send_comment_btn);
        listView = (ListView) findViewById(R.id.comments_listview);
        newComment = (EditText)  findViewById(R.id.new_comment);
        commentArrayAdapter= new CommentArrayAdapter(getApplicationContext(), R.layout.comment_item);
        for (int i = 0; i < 3; i++) {
            PitchComment c = new PitchComment(i, "no name", "no comment");
            commentArrayAdapter.add(c);
        }
        listView.setAdapter(commentArrayAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //******************kafri here the send btn*********
                //sent
                newComment.setText("");
            }
        });
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
