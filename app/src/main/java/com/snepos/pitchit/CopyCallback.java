package com.snepos.pitchit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by Omer on 22/07/2015.
 */
class CopyCallback implements ActionMode.Callback {

    private TextView mTextView;

    public  CopyCallback(TextView textView)
    {
        mTextView = textView;
    }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.copy, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        Context context = mTextView.getContext();

        int startSelection=mTextView.getSelectionStart();
        int endSelection=mTextView.getSelectionEnd();

        String selectedText = mTextView.getText().toString().substring(startSelection, endSelection);

        switch (item.getItemId()) {

            case R.id.copy:
                ClipboardManager clipboard = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", selectedText);
                clipboard.setPrimaryClip(clip);
                mode.finish();

                return true;
        }
        return false;
    }

    public void onDestroyActionMode(ActionMode mode) {
    }
}