package com.example.risingstar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {

    private int mCount;
    private MetaData[] mData;

    public MyAdapter(FragmentActivity fragmentActivity, int count, MetaData[] data) {
        super(fragmentActivity);
        mCount = count;
        mData = data;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ImageInfoFragment.newInstance(position + 1, mData[position]);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}
