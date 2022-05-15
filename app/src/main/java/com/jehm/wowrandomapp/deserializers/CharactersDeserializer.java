package com.jehm.wowrandomapp.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jehm.wowrandomapp.models.Character;

import java.lang.reflect.Type;
import java.util.List;

public class CharactersDeserializer implements JsonDeserializer<Character> {
    @Override
    public Character deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        int account_i = 0;
        int character_i = 0;

        int wowAccountID;
        String characterName;
        int characterID;
        String realmName;
        int realmID;
        String faction;

        wowAccountID = json.getAsJsonObject().get(String.valueOf(account_i))
                .getAsJsonObject().get("id").getAsInt();
        characterName = json.getAsJsonObject().get(String.valueOf(account_i))
                .getAsJsonObject().get(String.valueOf(character_i))
                .getAsJsonObject().get("name").getAsString();
        characterID = json.getAsJsonObject().get(String.valueOf(account_i))
                .getAsJsonObject().get(String.valueOf(character_i))
                .getAsJsonObject().get("id").getAsInt();
        realmName = json.getAsJsonObject().get(String.valueOf(account_i))
                .getAsJsonObject().get(String.valueOf(character_i))
                .getAsJsonObject().get("realm")
                .getAsJsonObject().get("name").getAsString();
        realmID = json.getAsJsonObject().get(String.valueOf(account_i))
                .getAsJsonObject().get(String.valueOf(character_i))
                .getAsJsonObject().get("realm")
                .getAsJsonObject().get("id").getAsInt();
        faction = json.getAsJsonObject().get(String.valueOf(account_i))
                .getAsJsonObject().get(String.valueOf(character_i))
                .getAsJsonObject().get("faction")
                .getAsJsonObject().get("name").getAsString();

//        do {
//
//
//        } while (wowAccountID);


        return new Character(wowAccountID, characterName, characterID, realmName, realmID, faction, 0);
    }
}
