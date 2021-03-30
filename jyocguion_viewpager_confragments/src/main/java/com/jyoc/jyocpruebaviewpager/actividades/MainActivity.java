package com.jyoc.jyocpruebaviewpager.actividades;

import android.os.Bundle;
import android.view.View;
import com.jyoc.jyocpruebaviewpager.R;
import com.jyoc.jyocpruebaviewpager.adaptadores.MiViewPagerAdapterForFragment;
import com.jyoc.jyocpruebaviewpager.fragments.Pantalla1_Fragment;
import com.jyoc.jyocpruebaviewpager.fragments.Pantalla2_fragment;
import com.jyoc.jyocpruebaviewpager.fragments.Pantalla3_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    private MiViewPagerAdapterForFragment mAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crea el adapter que devolverá un fragment para cada una de las tres secciones de la actividad.
        mAdapter = new MiViewPagerAdapterForFragment(getSupportFragmentManager());
        
        // añadimos los fragments al adapter para solo hacerlo una vez
        mAdapter.agregarFragment(new Pantalla1_Fragment());
        mAdapter.agregarFragment(new Pantalla2_fragment());
        mAdapter.agregarFragment(new Pantalla3_fragment());

        mViewPager = findViewById(R.id.view_pager_en_main);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // por aqui pasa al efectuar un movimiento entre paginas del viewPager
            }
            @Override
            public void onPageSelected(int position) {
               // por aqui pasa al posicionarse en una pagina del viewPager
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        // back pressed no sale, sino que vuele atras si no esta en la primera pagina
        if (mViewPager.getCurrentItem() < 1) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem((mViewPager.getCurrentItem()) - 1);
        }
    }
    
    public void onSiguiente(View v) {
        if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }
    public void onAnterior(View v) {
        if (mViewPager.getCurrentItem() ==0) {
            mViewPager.setCurrentItem(mViewPager.getAdapter().getCount()-1);
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
}