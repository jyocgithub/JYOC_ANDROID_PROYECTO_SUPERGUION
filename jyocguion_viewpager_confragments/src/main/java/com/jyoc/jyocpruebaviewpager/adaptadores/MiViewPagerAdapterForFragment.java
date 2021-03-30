package com.jyoc.jyocpruebaviewpager.adaptadores;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MiViewPagerAdapterForFragment extends FragmentPagerAdapter {
    private final List<Fragment> listaDeFragments = new ArrayList<>();

    public MiViewPagerAdapterForFragment(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        // este metodo coloca un fragment en el adapter
        // El fragmento que se instala se elige de la coleccion existente, segun su posicion.
        return listaDeFragments.get(position);
    }

    @Override
    public int getCount() {
        return listaDeFragments.size();
    }

    public void agregarFragment(Fragment fragment) {
        listaDeFragments.add(fragment);
    }
}