package com.snepos.pitchit;

/**
 * Created by user1 on 19/06/2015.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

public class FragmentTutorial extends Fragment {
    int position;
    ScrollView scrollView;
    ImageView img;



    public static final FragmentTutorial newInstance(int num)
    {
        FragmentTutorial f = new FragmentTutorial();
        Bundle bdl = new Bundle(2);
        bdl.putInt("position", num);
        f.setArguments(bdl);
        return f;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.tutorial_fragment_1, container, false);
        img = (ImageView) rootView.findViewById(R.id.img_tutorial);
        position = getArguments().getInt("position");


        switch (position)
        {
            case 0:
                img.setImageResource(R.drawable.to_1);
            break;
            case 1:
                img.setImageResource(R.drawable.to_2);
                break;
            case 2:
                img.setImageResource(R.drawable.to_3);
                break;
            case 3:
                img.setImageResource(R.drawable.to_4);
                break;
            case 4:
                img.setImageResource(R.drawable.to_5);
                break;
            case 5:
                img.setImageResource(R.drawable.rt_6);
                break;

        }

        return rootView;
    }

    }


