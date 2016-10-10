package org.koalanis.enki.hex;

/**
 * Created by kaleb on 8/10/16.
 */
public class HexTile {

    private HexGrid parent;

    public int row,col;

    public HexTile(HexGrid parent, int r, int c) {
        this.parent = parent;
        row = r;
        col = c;
    }

}
