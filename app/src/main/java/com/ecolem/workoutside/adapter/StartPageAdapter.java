package com.ecolem.workoutside.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by snabou on 02/11/2015.
 */
public class StartPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;


    public StartPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;

    }

    @Override

    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }


    @Override

    public int getCount() {
        return this.fragments.size();
    }

}
