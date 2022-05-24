package com.jehm.wowrandomapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.adapters.GoldAdapter;
import com.jehm.wowrandomapp.adapters.ViewPagerAdapter;
import com.jehm.wowrandomapp.models.Character;

import java.util.ArrayList;
import java.util.Map;

public class GoldFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public GoldFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);

        return view;
    }

    public void renderListFragment(FragmentActivity fragmentActivity, ArrayList<GoldAdapter> goldAdapters, ArrayList<Integer> wowAccountIDs) {
        viewPager2.setAdapter(new ViewPagerAdapter(fragmentActivity, goldAdapters));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Character character = (Character) goldAdapters.get(position).getItem(0);
                int ID = character.getWowAccountID();
                tab.setText("WoW " + (wowAccountIDs.indexOf(ID)+1));
            }
        });
        tabLayoutMediator.attach();
    }

}