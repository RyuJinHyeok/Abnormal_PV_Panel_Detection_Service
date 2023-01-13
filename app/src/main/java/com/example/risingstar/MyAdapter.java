package com.example.risingstar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {

    private int mCount;
    private Vertex[] vertex;

    public MyAdapter(FragmentActivity fragmentActivity, int count, Vertex[] v) {
        super(fragmentActivity);
        mCount = count;
        vertex = v;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ImageInfoFragment.newInstance(position + 1, vertex[position]);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}
