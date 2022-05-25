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
import com.jehm.wowrandomapp.models.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class GoldAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Character> characters;

    public GoldAdapter(Context context, int layout, final ArrayList<Character> characters) {
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
        String characterClass = characters.get(positon).getCharacterClass();
        String money = String.valueOf(characters.get(positon).getMoney());
        String[] moneyArray = renderMoney(money);
        String gold = new DecimalFormat("#,###.##").format(Integer.parseInt(moneyArray[0]));
        String silver = moneyArray[1];
        String bronze = moneyArray[2];

        setRealmTextSize(viewHolder, realm);
        setNameTextSize(viewHolder, name);
        setFactionColor(viewHolder, faction);
        setGoldTextSize(viewHolder, moneyArray[0]);

        viewHolder.textViewName.setText(name);
        viewHolder.textViewName.setTextColor(Color.parseColor(getClassColor(characterClass)));
        viewHolder.textViewRealm.setText(realm);
        viewHolder.textViewFaction.setText(faction);
        viewHolder.textViewGold.setText(gold);
        viewHolder.textViewSilver.setText(silver);
        viewHolder.textViewBronze.setText(bronze);

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

    private String getClassColor(String className) {
        switch (className) {
            case "Death Knight":
                return Constants.COLOR_DEATH_KNIGHT;
            case "Demon Hunter":
                return Constants.COLOR_DEMON_HUNTER;
            case "Druid":
                return Constants.COLOR_DRUID;
            case "Evoker":
                return Constants.COLOR_EVOKER;
            case "Hunter":
                return Constants.COLOR_HUNTER;
            case "Mage":
                return Constants.COLOR_MAGE;
            case "Monk":
                return Constants.COLOR_MONK;
            case "Paladin":
                return Constants.COLOR_PALADIN;
            case "Priest":
                return Constants.COLOR_PRIEST;
            case "Rogue":
                return Constants.COLOR_ROGUE;
            case "Shaman":
                return Constants.COLOR_SHAMAN;
            case "Warlock":
                return Constants.COLOR_WARLOCK;
            case "Warrior":
                return Constants.COLOR_WARRIOR;
            default:
                System.out.println("Class not found.");
                return "";
        }
    }

    private String[] renderMoney(String money) {
        String[] goldSilverBronze;
        String[] silverBronze;
        String[] wowGoldFormat = new String[3];
        if (money.length() >= 8) {
            goldSilverBronze = Utils.splitPrice(money, money.length() - 4);
            silverBronze = Utils.splitPrice(goldSilverBronze[1], 2);
            wowGoldFormat[0] = goldSilverBronze[0];
            wowGoldFormat[1] = silverBronze[0];
            wowGoldFormat[2] = silverBronze[1];
        } else if (money.length() == 7) {
            goldSilverBronze = Utils.splitPrice(money, 3);
            silverBronze = Utils.splitPrice(goldSilverBronze[1], 2);
            wowGoldFormat[0] = goldSilverBronze[0];
            wowGoldFormat[1] = silverBronze[0];
            wowGoldFormat[2] = silverBronze[1] + goldSilverBronze[2];
        } else if (money.length() == 6) {
            goldSilverBronze = Utils.splitPrice(money, 2);
            wowGoldFormat[0] = goldSilverBronze[0];
            wowGoldFormat[1] = goldSilverBronze[1];
            wowGoldFormat[2] = goldSilverBronze[2];
        }else  { // money.lenght() == 5
            goldSilverBronze = Utils.splitPrice(money, 3);
            silverBronze = Utils.splitPrice(goldSilverBronze[0], 1);
            wowGoldFormat[0] = silverBronze[0];
            wowGoldFormat[1] = silverBronze[1]+silverBronze[2];
            wowGoldFormat[2] = goldSilverBronze[1];
        }
        return wowGoldFormat;
    }

    private void setGoldTextSize(ViewHolder viewHolder, String gold){
//        <!--Para oro >= 1,000,000
//        android:textSize="12sp"-->
//        <!--Para oro >= 10,000,000
//        android:textSize="11sp"-->
        if(Integer.parseInt(gold) >= 1000000 && Integer.parseInt(gold) < 10000000){
            viewHolder.textViewGold.setTextSize(12);
        } else if(Integer.parseInt(gold) >= 10000000){
            viewHolder.textViewGold.setTextSize(11);
        } else {
            viewHolder.textViewGold.setTextSize(14);
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
