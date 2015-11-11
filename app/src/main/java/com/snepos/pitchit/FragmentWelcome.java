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

public class FragmentWelcome extends Fragment {
    int position;
    ScrollView scrollView;
    ImageView img;




    public static final FragmentWelcome newInstance(int num)
    {
        FragmentWelcome f = new FragmentWelcome();
        Bundle bdl = new Bundle(2);
        bdl.putInt("position", num);
        f.setArguments(bdl);
        return f;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView;
        position = getArguments().getInt("position");

            rootView  = (ViewGroup) inflater.inflate(
                    R.layout.tutorial_fragment_1, container, false);
            img = (ImageView) rootView.findViewById(R.id.img_tutorial);

            switch (position) {
                case 0:
                    img.setImageResource(R.drawable.t1);
                    break;
                case 1:
                    img.setImageResource(R.drawable.t2);
                    break;
                case 2:
                    img.setImageResource(R.drawable.t3);
                    break;
                case 3:
                    img.setImageResource(R.drawable.t4);
                    break;

            }


        return rootView;
    }

    }


