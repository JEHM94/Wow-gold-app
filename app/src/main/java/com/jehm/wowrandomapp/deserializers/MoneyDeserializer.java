package com.jehm.wowrandomapp.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jehm.wowrandomapp.models.Character;

import java.lang.reflect.Type;

public class MoneyDeserializer implements JsonDeserializer<Character> {
    @Override
    public Character deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        long money = json.getAsJsonObject().get("money").getAsLong();
        return new Character(0, "characterName", 0, "realmName", 0, "faction","characterClass", money);
    }
}
