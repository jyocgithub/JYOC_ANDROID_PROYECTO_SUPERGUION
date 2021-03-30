package com.jyoc.jyocpruebaviewpager.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyoc.jyocpruebaviewpager.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pantalla2_fragment extends Fragment {

    //public Pantalla1_fragment() {
    //    // Required empty public constructor
    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantalla2, container, false);
        return  view;
    }

}
