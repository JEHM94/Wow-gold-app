package com.jehm.wowrandomapp.models;

import java.util.ArrayList;

public class Character {
    private final int wowAccountID;

    private final String characterName;
    private final int characterID;

    private final String realmName;
    private final int realmID;

    private final String faction;
    private final String characterClass;
    private long money;

    private ArrayList<Character> characterList;

    public Character(int wowAccountID, String characterName, int characterID, String realmName, int realmID, String faction, String characterClass, long money) {
        this.wowAccountID = wowAccountID;
        this.characterName = characterName;
        this.characterID = characterID;
        this.realmName = realmName;
        this.realmID = realmID;
        this.faction = faction;
        this.characterClass = characterClass;
        this.money = money;
    }

    public ArrayList<Character> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(ArrayList<Character> characterList) {
        this.characterList = characterList;
    }

    public int getWowAccountID() {
        return wowAccountID;
    }

    public String getCharacterName() {
        return characterName;
    }

    public int getCharacterID() {
        return characterID;
    }

    public String getRealmName() {
        return realmName;
    }

    public int getRealmID() {
        return realmID;
    }

    public String getFaction() {
        return faction;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
