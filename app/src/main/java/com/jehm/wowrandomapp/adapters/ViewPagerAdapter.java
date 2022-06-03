package com.jehm.wowrandomapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jehm.wowrandomapp.fragments.ListFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final ArrayList<GoldAdapter> goldAdapters;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<GoldAdapter> goldAdapters) {
        super(fragmentActivity);
        this.goldAdapters = goldAdapters;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ListFragment(goldAdapters.get(position));
    }

    @Override
    public int getItemCount() {
        return goldAdapters.size();
    }
}
