package com.jehm.wowrandomapp.models;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Window;
import android.view.WindowManager;

import com.jehm.wowrandomapp.R;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String[] splitPrice(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }

    public static void transparentStatusBar(Activity activity) {
        Window w = activity.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void removeSharedPreferences(SharedPreferences preferences) {
        preferences.edit().clear().apply();
    }

    public static int getBattletagImage(String battletag) {
        switch (battletag) {
            case "Myth#1451":
                return R.mipmap.profile_myth_background;
            case "Maxius#11621":
                return R.mipmap.profile_zood_background;
            case "Loe#12285":
                return R.mipmap.profile_loe_background;
            case "Boweins#1330":
                return R.mipmap.profile_bowein_background;
            case "Sorem#11138":
                return R.mipmap.profile_sorem_background;
            default:
                return R.mipmap.profile_battlenet_background;
        }
    }

    public static String[] renderMoney(String money) {
        String[] goldSilverBronze;
        String[] silverBronze;
        String[] wowGoldFormat = new String[3];
        if (money.length() >= 8) {
            goldSilverBronze = splitPrice(money, money.length() - 4);
            silverBronze = splitPrice(goldSilverBronze[1], 2);
            wowGoldFormat[0] = goldSilverBronze[0];
            wowGoldFormat[1] = silverBronze[0];
            wowGoldFormat[2] = silverBronze[1];
        } else if (money.length() == 7) {
            goldSilverBronze = splitPrice(money, 3);
            silverBronze = splitPrice(goldSilverBronze[1], 2);
            wowGoldFormat[0] = goldSilverBronze[0];
            wowGoldFormat[1] = silverBronze[0];
            wowGoldFormat[2] = silverBronze[1] + goldSilverBronze[2];
        } else if (money.length() == 6) {
            goldSilverBronze = splitPrice(money, 2);
            wowGoldFormat[0] = goldSilverBronze[0];
            wowGoldFormat[1] = goldSilverBronze[1];
            wowGoldFormat[2] = goldSilverBronze[2];
        }else  { // money.lenght() == 5
            goldSilverBronze = splitPrice(money, 3);
            silverBronze = splitPrice(goldSilverBronze[0], 1);
            wowGoldFormat[0] = silverBronze[0];
            wowGoldFormat[1] = silverBronze[1]+silverBronze[2];
            wowGoldFormat[2] = goldSilverBronze[1];
        }
        return wowGoldFormat;
    }
}
