package com.jehm.wowrandomapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.adapters.GoldAdapter;
import com.jehm.wowrandomapp.models.Character;
import com.jehm.wowrandomapp.models.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    private GoldAdapter goldAdapter;
    private long totalGold = 0;
    private ListView listView;
    private TextView textViewTotalGold;
    private TextView textViewTotalSilver;
    private TextView textViewTotalBronze;

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

        bindUI(view);
        listView.setAdapter(goldAdapter);

        String[] moneyArray = Utils.renderMoney(getTotalMoney());
        String gold = new DecimalFormat("#,###.##").format(Integer.parseInt(moneyArray[0]));
        String silver = moneyArray[1];
        String bronze = moneyArray[2];

        textViewTotalGold.setText(gold);
        textViewTotalSilver.setText(silver);
        textViewTotalBronze.setText(bronze);

        return view;
    }

    private void bindUI(View view) {
        listView = (ListView) view.findViewById(R.id.listViewGold);
        textViewTotalGold = (TextView) view.findViewById(R.id.textViewTotalGold);
        textViewTotalSilver = (TextView) view.findViewById(R.id.textViewTotalSilver);
        textViewTotalBronze = (TextView) view.findViewById(R.id.textViewTotalBronze);
    }

    private String getTotalMoney() {
        ArrayList<Character> characters = goldAdapter.getCharacters();
        for (int i = 0; i < characters.size(); i++) {
            totalGold += characters.get(i).getMoney();
        }
        return String.valueOf(totalGold);
    }
}