package com.example.risingstar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {

    private int mCount;
    private DataInfo[] dataInfos;

    public MyAdapter(FragmentActivity fragmentActivity, int count, DataInfo[] d) {
        super(fragmentActivity);
        mCount = count;
        dataInfos = d;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ImageInfoFragment.newInstance(position + 1, dataInfos[position]);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}
