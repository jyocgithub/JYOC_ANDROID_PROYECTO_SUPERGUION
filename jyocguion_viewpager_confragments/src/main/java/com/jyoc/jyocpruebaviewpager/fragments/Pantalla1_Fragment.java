package com.jyoc.jyocpruebaviewpager.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jyoc.jyocpruebaviewpager.R;

import androidx.fragment.app.Fragment;
public class Pantalla1_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantalla1, container, false);
        return  view;
    }
}
