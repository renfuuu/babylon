package org.tardibear.enki;

/**
 * Created by itoro on 11/5/16.
 * Simple enum class for the loot
 */


public class TreasureChest {

    private final int size;

    public enum Loot {
        PISTOL, KNIFE, MEDKIT, PISTOL_AMMO, CROWBAR, AK_47, WALKIE_TALKIE, BINOCULARS, AK_47_AMMO
    }

    public TreasureChest(){
        size = 9;
    }

    public int getSize(){
        return size;
    }
}
