package com.snepos.pitchit;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 12/06/2015.
 */
public class CardArrayAdapter  extends ArrayAdapter<Card> {
    private static final String TAG = "CardArrayAdapter";
    private List<Card> cardList = new ArrayList<Card>();
    Context myContext;
    public CardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        myContext = context;
    }

    @Override
    public void add(Card card) {
        cardList.add(card);

        //super.add(card);
    }

    @Override
    public void clear() {
        cardList.clear();
        //super.clear();
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pitch_item, parent, false);
        }

        final LinearLayout outerLinearLayout = (LinearLayout) view.findViewById(R.id.outerLayout);
        outerLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        final TextView Head = (TextView) view.findViewById(R.id.HeadName);
        final TextView Publisher = (TextView) view.findViewById(R.id.PublisherName);
        final TextView Body = (TextView) view.findViewById(R.id.Body);
        final Button Like = (Button) view.findViewById(R.id.button_liked);
        final Button Report = (Button) view.findViewById(R.id.button_report);
        final Button OnIt = (Button) view.findViewById(R.id.button_OnIt);

        Head.setCustomSelectionActionModeCallback(new CopyCallback(Head));
        Publisher.setCustomSelectionActionModeCallback(new CopyCallback(Publisher));
        Body.setCustomSelectionActionModeCallback(new CopyCallback(Body));

        final TextView UpVotes = ((TextView) view.findViewById(R.id.up_votes));
        final TextView OnItVotes = ((TextView) view.findViewById(R.id.on_it_votes));

        final Card currentCard = cardList.get(position);

        Head.setText(currentCard.getHead());
        Publisher.setText(currentCard.getPublisherName());
        Body.setText(currentCard.getBody());

        UpVotes.setText(String.valueOf(currentCard.getUpVotes()));
        OnItVotes.setText(String.valueOf(currentCard.getOnItVotes()));

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                currentCard.setClicks(currentCard.getClickes() + 1);
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        currentCard.setClicks(0);
                    }
                };

                if (currentCard.getClickes() == 1)

                {
                    //Single click
                    handler.postDelayed(r, 250);
                } else if (currentCard.getClickes() == 2)

                {
                    //Double click
                    currentCard.setClicks(0);
                    currentCard.clickedLike();
                    if (currentCard.getIsLiked()) {
                        Like.setBackgroundResource(R.drawable.like_icon_16);
                        UpVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) + 1));
                    } else {
                        Like.setBackgroundResource(R.drawable.like_icon_16_blackend);
                        UpVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) - 1));
                    }
                }

            }

        });


        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCard.clickedLike();
                if (currentCard.getIsLiked()) {
                    Like.setBackgroundResource(R.drawable.like_icon_16);
                    UpVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) + 1));
                } else {
                    Like.setBackgroundResource(R.drawable.like_icon_16_blackend);
                    UpVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) - 1));
                }
            }
        });
        if(currentCard.getIsLiked())
            Like.setBackgroundResource(R.drawable.like_icon_16);
        else
            Like.setBackgroundResource(R.drawable.like_icon_16_blackend);

        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCard.clickedReport();
                if (currentCard.getIsReport()) {
                    Report.setBackgroundResource(R.drawable.flag_pressed);
                } else
                {
                    Report.setBackgroundResource(R.drawable.flag_upressed);
                }
            }
        });
        if(currentCard.getIsReport())
            Report.setBackgroundResource(R.drawable.flag_pressed);
        else
            Report.setBackgroundResource(R.drawable.flag_upressed);

        OnIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCard.clickedOnIt();
                if(currentCard.getIsOnIt()){
                    OnIt.setBackgroundResource(R.drawable.on_it_pressed);
                    OnItVotes.setText(String.valueOf(Integer.parseInt(OnItVotes.getText().toString()) + 1));
                }
                else{
                    OnIt.setBackgroundResource(R.drawable.on_it_unpressed);
                    OnItVotes.setText(String.valueOf(Integer.parseInt(OnItVotes.getText().toString()) - 1));
                }
            }
        });
        if(currentCard.getIsOnIt())
        {
            OnIt.setBackgroundResource(R.drawable.on_it_pressed);
            //OnItVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) + 1));
        }
        else{
            OnIt.setBackgroundResource(R.drawable.on_it_unpressed);
            //OnItVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) - 1));
        }

        return view;
    }
}

