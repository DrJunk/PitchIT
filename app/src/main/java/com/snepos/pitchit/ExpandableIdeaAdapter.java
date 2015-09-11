package com.snepos.pitchit;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snepos.pitchit.database.IdeaData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Omer on 31/08/2015.
 */
public class ExpandableIdeaAdapter extends RecyclerView.Adapter<ExpandableIdeaAdapter.ViewHolder>
{
    private IdeaData[] mDataset;

    ExpandableIdeaUtil.KeepOneOpen<ViewHolder> keepOne = new ExpandableIdeaUtil.KeepOneOpen<ViewHolder>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExpandableIdeaAdapter(IdeaData[] expandableIdeas) {
        refreshDataset(expandableIdeas);
    }

    public void refreshDataset(IdeaData[] expandableIdeas)
    {
        mDataset = new IdeaData[expandableIdeas.length];
        for(int i = 0; i < expandableIdeas.length; i++)
        {
            mDataset[i] = expandableIdeas[expandableIdeas.length - 1 - i];
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExpandableIdeaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expandeable_idea, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.bind(position, mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableIdeaUtil.Expandable{
        // each data item is just a string in this case
        public View topView;
        public View bottomView;
        public AnimatorSet animatorSet;
        public ViewHolder(View itemView) {
            super(itemView);
            topView = itemView.findViewById(R.id.expandableIdea_topLayout);
            bottomView = itemView.findViewById(R.id.expandableIdea_bottomLayout);

            itemView.setOnClickListener(this);
        }

        public void bind(int pos, IdeaData ideaData) {
            //Fill card
            ((TextView)itemView.findViewById(R.id.expandableIdea_title)).setText(ideaData.title);
            ((TextView)itemView.findViewById(R.id.expandableIdea_publisherName)).setText(ideaData.publisherName);
            ((TextView)itemView.findViewById(R.id.expandableIdea_body)).setText(ideaData.text);

            int sum = 5000;
            for (int i=0; i< ideaData.publisherName.length(); i++)
            {
                sum+= ideaData.publisherName.charAt(i);
            }
            sum *= sum;
            final Integer temp = (CardArrayAdapter.matColors.get(sum % CardArrayAdapter.matColors.size()));
            topView.setBackgroundColor(temp);

            keepOne.bind(this, pos);
        }

        @Override
        public void onClick(View v) {
            keepOne.toggle(this);
        }

        @Override
        public View getBottomView() {
            return bottomView;
        }

        @Override
        public View getTopView(){return topView;}
    }
}
