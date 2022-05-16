package com.jehm.wowrandomapp.models;

import java.util.List;

public class CharacterList {
    private List<Character> characterList;

    public CharacterList(List<Character> characterList) {
        this.characterList = characterList;
    }

    public List<Character> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(List<Character> characterList) {
        this.characterList = characterList;
    }
}
