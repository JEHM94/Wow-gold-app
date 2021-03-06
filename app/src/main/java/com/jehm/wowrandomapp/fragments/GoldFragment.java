package com.jehm.wowrandomapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.adapters.GoldAdapter;
import com.jehm.wowrandomapp.adapters.ViewPagerAdapter;
import com.jehm.wowrandomapp.models.Character;

import java.util.ArrayList;

public class GoldFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private SortGoldListener callback;

    public GoldFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (SortGoldListener) context;
        }catch (Exception e){
            throw new ClassCastException(context + "Should implement SortGoldListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);
        TextView headerGold = view.findViewById(R.id.textViewHeaderGold);
        TextView headerRealm = view.findViewById(R.id.textViewHeaderRealm);
        TextView headerName = view.findViewById(R.id.textViewHeaderName);

        headerGold.setOnClickListener(view1 -> callback.sortList(R.string.gold));
        headerRealm.setOnClickListener(view12 -> callback.sortList(R.string.realm));
        headerName.setOnClickListener(view13 -> callback.sortList(R.string.name));

        return view;
    }

    public void renderListFragment(FragmentActivity fragmentActivity, ArrayList<GoldAdapter> goldAdapters, ArrayList<Integer> wowAccountIDs) {
        viewPager2.setAdapter(new ViewPagerAdapter(fragmentActivity, goldAdapters));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            Character character = goldAdapters.get(position).getCharacters().get(0);
            int ID = character.getWowAccountID();
            tab.setText("WoW " + (wowAccountIDs.indexOf(ID) + 1));
        });
        tabLayoutMediator.attach();
    }

    public interface SortGoldListener{
        void sortList(int column);
    }
}