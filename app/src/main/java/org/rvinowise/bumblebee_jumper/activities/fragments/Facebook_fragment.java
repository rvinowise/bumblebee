package org.rvinowise.bumblebee_jumper.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rvinowise.bumblebee_jumper.R;
import org.rvinowise.bumblebee_jumper.activities.Game_menu_activity;


public class Facebook_fragment extends Fragment {



    Game_menu_activity game_menu_activity;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.facebook_fragment, container, false);
    }
}
