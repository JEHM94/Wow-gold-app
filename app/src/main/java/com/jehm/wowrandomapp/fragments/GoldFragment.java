package com.jehm.wowrandomapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.adapters.GoldAdapter;

import java.util.ArrayList;

public class GoldFragment extends Fragment {

    public GoldFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold, container, false);

        return view;
    }

    public void renderListFragment(GoldAdapter goldAdapter) {
        ListFragment fragment = (ListFragment) getChildFragmentManager().findFragmentById(R.id.listFragment);
        fragment.renderCharacterList(goldAdapter);
    }

}