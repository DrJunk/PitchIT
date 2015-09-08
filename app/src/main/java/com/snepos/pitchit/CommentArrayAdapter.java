package com.snepos.pitchit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user1 on 22/08/2015.
 */
public class CommentArrayAdapter extends ArrayAdapter<PitchComment> {
private static final String TAG = "CardArrayAdapter";
private List<PitchComment> commentList ;
        Context myContext;
    Resources resources;

    public CommentArrayAdapter(Context context, int textViewResourceId,    Resources resources){
        super(context, textViewResourceId);
        myContext = context;
        commentList = new ArrayList<PitchComment>();
        this.resources=resources;
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
        RoundImage roundedImage;
        final TextView Head = (TextView) view.findViewById(R.id.comment_head);
        final TextView Body = (TextView) view.findViewById(R.id.comment_body);
        final ImageView AccountPicture = (ImageView) view.findViewById(R.id.comment_image);
        Bitmap bm = BitmapFactory.decodeResource(resources, R.drawable.account_image);
        roundedImage = new RoundImage(bm);
        AccountPicture.setImageDrawable(roundedImage);

        final PitchComment currentComment = commentList.get(position);

        Head.setText(currentComment.getUserNickname());
        Body.setText(currentComment.getText());
        return view;
    }

}
