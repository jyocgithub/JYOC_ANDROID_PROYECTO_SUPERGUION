package com.jyoc.jyocpruebaviewpager;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.vpPrincipal);
        mViewPager.setAdapter(new MiPageAdapter(this));
        mViewPager.setCurrentItem(1);
    }

    public void onSiguiente(View v) {
        if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount()-1) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
            //mViewPager.setCurrentItem(
            //        (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount()-1) ? 0 :  mViewPager.getCurrentItem() + 1
            //);
    }
}