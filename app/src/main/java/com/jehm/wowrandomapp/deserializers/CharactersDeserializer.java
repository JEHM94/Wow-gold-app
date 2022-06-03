package com.jehm.wowrandomapp.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jehm.wowrandomapp.models.Character;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CharactersDeserializer implements JsonDeserializer<Character> {
    @Override
    public Character deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        int account_i = 0;
        int character_i = 0;

        ArrayList<Character> characterList = new ArrayList<>();
        int wowAccountsLength = json.getAsJsonObject().get("wow_accounts").getAsJsonArray().size();
        while (account_i < wowAccountsLength) {
            int wowAccountID = json
                    .getAsJsonObject().get("wow_accounts")
                    .getAsJsonArray().get(account_i)
                    .getAsJsonObject().get("id").getAsInt();

            int characterLenght = json.getAsJsonObject().get("wow_accounts")
                    .getAsJsonArray().get(account_i)
                    .getAsJsonObject().get("characters").getAsJsonArray().size();

            while (character_i < characterLenght) {
                String characterName = json
                        .getAsJsonObject().get("wow_accounts")
                        .getAsJsonArray().get(account_i)
                        .getAsJsonObject().get("characters")
                        .getAsJsonArray().get(character_i)
                        .getAsJsonObject().get("name").getAsString();
                int characterID = json
                        .getAsJsonObject().get("wow_accounts")
                        .getAsJsonArray().get(account_i)
                        .getAsJsonObject().get("characters")
                        .getAsJsonArray().get(character_i)
                        .getAsJsonObject().get("id").getAsInt();
                String realmName = json
                        .getAsJsonObject().get("wow_accounts")
                        .getAsJsonArray().get(account_i)
                        .getAsJsonObject().get("characters")
                        .getAsJsonArray().get(character_i)
                        .getAsJsonObject().get("realm")
                        .getAsJsonObject().get("name").getAsString();
                int realmID = json
                        .getAsJsonObject().get("wow_accounts")
                        .getAsJsonArray().get(account_i)
                        .getAsJsonObject().get("characters")
                        .getAsJsonArray().get(character_i)
                        .getAsJsonObject().get("realm")
                        .getAsJsonObject().get("id").getAsInt();
                String faction = json
                        .getAsJsonObject().get("wow_accounts")
                        .getAsJsonArray().get(account_i)
                        .getAsJsonObject().get("characters")
                        .getAsJsonArray().get(character_i)
                        .getAsJsonObject().get("faction")
                        .getAsJsonObject().get("name").getAsString();
                String characterClass = json
                        .getAsJsonObject().get("wow_accounts")
                        .getAsJsonArray().get(account_i)
                        .getAsJsonObject().get("characters")
                        .getAsJsonArray().get(character_i)
                        .getAsJsonObject().get("playable_class")
                        .getAsJsonObject().get("name").getAsString();

                Character character = new Character(wowAccountID, characterName, characterID, realmName, realmID, faction, characterClass, 0);
                characterList.add(character);
                character_i++;
            }
            character_i = 0;
            account_i++;
        }
        Character character = new Character(0, "characterName", 0, "realmName", 0, "faction", "characterClass", 0);
        character.setCharacterList(characterList);
        return character;
    }
}
