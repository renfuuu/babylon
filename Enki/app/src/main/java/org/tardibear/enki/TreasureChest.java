package org.tardibear.enki;

/**
 * Created by itoro on 11/5/16.
 * Simple enum class for the loot and letters
 */


public class TreasureChest {

    private final int lootSize;
    private final int numLetters;

    public enum Loot {
        PISTOL, KNIFE, MEDKIT, PISTOL_AMMO, CROWBAR, AK_47, WALKIE_TALKIE, BINOCULARS, AK_47_AMMO
    }

    public enum Letters {
        LETTER_1
    }

    public TreasureChest(){
        lootSize = 9;
        numLetters = 1;
    }

    public int getLootSize(){
        return lootSize;
    }

    public int getNumLetters(){
        return numLetters;
    }
}
