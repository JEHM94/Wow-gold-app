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

    private ListView listView;

    public GoldFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold, container, false);
        listView = (ListView) view.findViewById(R.id.listViewGold);
/*
        ArrayList<String> names = new ArrayList<>();
        names.add("Loe");
        names.add("Doomi");
        names.add("Duhh");
        names.add("Loewiu");
        names.add("Aennoxen");
        names.add("Loe");
        names.add("Doomi");
        names.add("Duhh");
        names.add("Loewiu");
        names.add("Aennoxen");
        names.add("Loe");
        names.add("Doomi");
        names.add("Duhh");
        names.add("Loewiu");
        names.add("Aennoxen");
        names.add("Loe");
        names.add("Doomi");
        names.add("Duhh");
        names.add("Loewiu");
        names.add("Aennoxen");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after xx seconds


            }
        }, 6000);*/



        return view;
    }
    public void renderCharacterList (GoldAdapter goldAdapter){
        listView.setAdapter(goldAdapter);
    }
}