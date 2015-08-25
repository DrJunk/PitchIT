package com.snepos.pitchit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 22/08/2015.
 */
public class CommentArrayAdapter extends ArrayAdapter<PitchComment> {
private static final String TAG = "CardArrayAdapter";
private List<PitchComment> commentList = new ArrayList<PitchComment>();
        Context myContext;

    public CommentArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        myContext = context;
    }
    @Override
    public void add(PitchComment comment) {
        commentList.add(comment);

        //super.add(card);
    }

    @Override
    public void clear() {
        commentList.clear();
        //super.clear();
    }

    @Override
    public int getCount() {
        return this.commentList.size();
    }
    @Override
    public PitchComment getItem(int index) {
        return this.commentList.get(index);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comment_item, parent, false);
        }
        final TextView Head = (TextView) view.findViewById(R.id.comment_head);
        final TextView Body = (TextView) view.findViewById(R.id.comment_body);

        final PitchComment currentComment = commentList.get(position);

        Head.setText(currentComment.getUserNickname());
        Body.setText(currentComment.getText());
        return view;
    }

}
