package com.jehm.wowrandomapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jehm.wowrandomapp.R;

import java.util.ArrayList;
import java.util.List;

public class GoldFragment extends Fragment {

    private ListView listView;

    public GoldFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold, container, false);
        listView = (ListView) view.findViewById(R.id.listViewGold);

        List<String> names = new ArrayList<String>();
        names.add("1");
        names.add("22");
        names.add("333");
        names.add("4444");
        names.add("55555");

        return view;
    }
}