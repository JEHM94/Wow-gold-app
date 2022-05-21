package com.jehm.wowrandomapp.adapters;

import static com.jehm.wowrandomapp.constants.Constants.COLOR_ALLIANCE;
import static com.jehm.wowrandomapp.constants.Constants.COLOR_HORDE;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.constants.Constants;
import com.jehm.wowrandomapp.models.Character;

import java.util.ArrayList;
import java.util.Objects;

public class GoldAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Character> characters;

    public GoldAdapter(Context context, int layout, ArrayList<Character> characters) {
        this.context = context;
        this.layout = layout;
        this.characters = characters;
    }

    @Override
    public int getCount() {
        return this.characters.size();
    }

    @Override
    public Object getItem(int position) {
        return this.characters.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.character_gold_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.textViewFaction = (TextView) convertView.findViewById(R.id.textViewFaction);
            viewHolder.textViewRealm = (TextView) convertView.findViewById(R.id.textViewRealm);
            viewHolder.textViewBronze = (TextView) convertView.findViewById(R.id.textViewBronze);
            viewHolder.textViewSilver = (TextView) convertView.findViewById(R.id.textViewSilver);
            viewHolder.textViewGold = (TextView) convertView.findViewById(R.id.textViewGold);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String name = characters.get(positon).getCharacterName();
        String faction = (Objects.equals(characters.get(positon).getFaction(), "Horde")) ? "[H]" : "[A]";
        String realm = characters.get(positon).getRealmName();
        String money = String.valueOf(characters.get(positon).getMoney());
        String bronze;
        String silver;
        String gold;


        setRealmTextSize(viewHolder, realm);
        setNameTextSize(viewHolder, name);
        setFactionColor(viewHolder, faction);

        viewHolder.textViewName.setText(name);
        viewHolder.textViewRealm.setText(realm);
        viewHolder.textViewFaction.setText(faction);

        return convertView;
    }

    private void setRealmTextSize(ViewHolder viewHolder, String realm) {
        if (realm.length() > 8 && realm.length() <= 10) {
            viewHolder.textViewRealm.setTextSize(14);
        } else if (realm.length() > 10) {
            viewHolder.textViewRealm.setTextSize(13);
        } else {
            viewHolder.textViewRealm.setTextSize(16);
        }
    }

    private void setNameTextSize(ViewHolder viewHolder, String name) {
        if (name.length() > 9 && name.length() <= 11) {
            viewHolder.textViewName.setTextSize(14);
        } else if (name.length() > 11) {
            viewHolder.textViewName.setTextSize(12);
        } else {
            viewHolder.textViewName.setTextSize(16);
        }
    }

    private void setFactionColor(ViewHolder viewHolder, String faction) {
        if (faction.equals("[H]")) {
            viewHolder.textViewFaction.setTextColor(Color.parseColor(COLOR_HORDE));
        } else {
            viewHolder.textViewFaction.setTextColor(Color.parseColor(COLOR_ALLIANCE));
        }
    }

    static class ViewHolder {
        private TextView textViewName;
        private TextView textViewFaction;
        private TextView textViewRealm;
        private TextView textViewBronze;
        private TextView textViewSilver;
        private TextView textViewGold;
    }
}
