package com.jehm.wowrandomapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.adapters.GoldAdapter;

public class ListFragment extends Fragment {

    private ListView listView;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.listViewGold);
        return view;
    }
    public void renderCharacterList (GoldAdapter goldAdapter){
        listView.setAdapter(goldAdapter);
    }
}