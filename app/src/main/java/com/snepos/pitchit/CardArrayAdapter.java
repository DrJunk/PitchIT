package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    int sum = 5000;
    int red;
    int blue;
    int green;
    static List<Integer> matColors;
    public CardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        myContext = context;
        matColors = new ArrayList<Integer>();
        matColors.add(Color.parseColor("#96F44336"));
        matColors.add(Color.parseColor("#96E91E63"));
        matColors.add(Color.parseColor("#969C27B0"));
        matColors.add(Color.parseColor("#96673AB7"));
        matColors.add(Color.parseColor("#963F51B5"));
        matColors.add(Color.parseColor("#962196F3"));
        matColors.add(Color.parseColor("#9603A9F4"));
        matColors.add(Color.parseColor("#9600BCD4"));
        matColors.add(Color.parseColor("#96009688"));
        matColors.add(Color.parseColor("#964CAF50"));
        matColors.add(Color.parseColor("#968BC34A"));
        matColors.add(Color.parseColor("#96CDDC39"));
        matColors.add(Color.parseColor("#96FFEB3B"));
        matColors.add(Color.parseColor("#96FFC107"));
        matColors.add(Color.parseColor("#96FF9800"));
        matColors.add(Color.parseColor("#96FF5722"));
        matColors.add(Color.parseColor("#96795548"));
        matColors.add(Color.parseColor("#969E9E9E"));
        matColors.add(Color.parseColor("#96607D8B"));

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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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


        final LinearLayout top = (LinearLayout) view.findViewById(R.id.top_layout);
        final LinearLayout card = (LinearLayout) view.findViewById(R.id.middle_card_layout);

        Head.setCustomSelectionActionModeCallback(new CopyCallback(Head));
        Publisher.setCustomSelectionActionModeCallback(new CopyCallback(Publisher));
        Body.setCustomSelectionActionModeCallback(new CopyCallback(Body));

        final TextView UpVotes = ((TextView) view.findViewById(R.id.up_votes));
        final TextView OnItVotes = ((TextView) view.findViewById(R.id.on_it_votes));

        final Card currentCard = cardList.get(position);

        Head.setText(currentCard.getHead());
        Publisher.setText(currentCard.getPublisherName());
        Body.setText(currentCard.getBody());

        sum = 5000;
        for (int i=0; i< Publisher.length(); i++)
        {
            sum+= Publisher.getText().toString().charAt(i);
        }
        //cardTop
        sum *= sum;
       /* red= sum % 151;
        green = sum % 152;
        blue =sum % 154;
        if (green < red &&green < blue )
            green= green%2;
        if (blue < green &&blue < red )
            blue= blue%2;
        if (red < green &&red < blue )
            red= red%2;*/
        final Integer temp = (matColors.get(sum % matColors.size()));
        top.setBackgroundColor(new Integer(temp));



        UpVotes.setText(String.valueOf(currentCard.getUpVotes()));
        OnItVotes.setText(String.valueOf(currentCard.getOnItVotes()));

        view.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                        card.setBackgroundColor(temp);
                        card.getBackground().setAlpha(70);
                        Like.setBackgroundResource(R.drawable.bulb_on);
                        UpVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) + 1));
                    } else {
                        card.setBackground(null);
                        Like.setBackgroundResource(R.drawable.bulb_off);
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
                    card.setBackgroundColor(temp);
                    card.getBackground().setAlpha(70);
                    Like.setBackgroundResource(R.drawable.bulb_on);
                    UpVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) + 1));
                } else {
                    card.setBackground(null);
                    Like.setBackgroundResource(R.drawable.bulb_off);
                    UpVotes.setText(String.valueOf(Integer.parseInt(UpVotes.getText().toString()) - 1));
                }
            }
        });
        if(currentCard.getIsLiked()) {
            card.setBackgroundColor(temp);
            card.getBackground().setAlpha(70);
            Like.setBackgroundResource(R.drawable.bulb_on);
        }
        else {
            Like.setBackgroundResource(R.drawable.bulb_off);
            card.setBackground(null);
        }

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
        top.setBackgroundColor(matColors.get(sum % matColors.size()));
        return view;
    }
}

