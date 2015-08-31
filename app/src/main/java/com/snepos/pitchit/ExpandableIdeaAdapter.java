package com.snepos.pitchit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Omer on 31/08/2015.
 */
public class ExpandableIdeaAdapter extends RecyclerView.Adapter<ExpandableIdeaAdapter.ViewHolder>
{
    private ExpandableIdea[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public TextView mTitleView;
        public ViewHolder(LinearLayout v) {
            super(v);
            mView = v;
            mTitleView = (TextView)v.findViewById(R.id.expandeable_idea_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExpandableIdeaAdapter(ExpandableIdea[] expandableIdeas) {
        refreshDataset(expandableIdeas);
    }

    public void refreshDataset(ExpandableIdea[] expandableIdeas)
    {
        mDataset = expandableIdeas;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExpandableIdeaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expandeable_idea, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((LinearLayout)v.findViewById(R.id.expandeable_idea_main));
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.mTitleView.setText(mDataset[position].getTitle());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
