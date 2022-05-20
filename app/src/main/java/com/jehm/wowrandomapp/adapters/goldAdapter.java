package com.jehm.wowrandomapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class goldAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Character> list;

    public goldAdapter(Context context, int layout, ArrayList<Character> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup viewGroup) {
        return null;
    }
}
