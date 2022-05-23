package com.jehm.wowrandomapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.adapters.GoldAdapter;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private ListView listView;
    private GoldAdapter goldAdapter;

    public ListFragment() {
        // Required empty public constructor
    }
    public ListFragment(GoldAdapter goldAdapter) {
        this.goldAdapter = goldAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.listViewGold);
        listView.setAdapter(goldAdapter);
        return view;
    }
}