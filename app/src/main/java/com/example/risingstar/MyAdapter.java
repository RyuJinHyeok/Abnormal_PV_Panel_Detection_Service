package com.example.risingstar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {

    private int mCount;
    private Location[] mLocations;

    public MyAdapter(FragmentActivity fragmentActivity, int count, Location[] locations) {
        super(fragmentActivity);
        mCount = count;
        mLocations = locations;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return ImageInfoFragment.newInstance(position + 1, mLocations[position]);

    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}