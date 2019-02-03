package com.doodee.voiceclicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentClicker extends Fragment {
    public String title;

    public static FragmentClicker newInstance(String title) {
        FragmentClicker fragmentClicker = new FragmentClicker();
        Bundle args = new Bundle();
        args.putString("argsTitle", title);
        fragmentClicker.setArguments(args);
        return fragmentClicker;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments() != null ? getArguments().getString("argsTitle") : "empty";
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_clicker, container, false);
        return view;
    }


}
