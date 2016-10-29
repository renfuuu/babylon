package org.tardibear.enki.hex;

/**
 * Created by kaleb on 8/10/16.
 */
public class HexTile {

    private HexGrid parent;

    // in odd-r offset coordinates
    public int row,col;

    public float[] color;

    public HexTile(HexGrid parent, int r, int c, float[] color) {
        this.parent = parent;
        row = r;
        col = c;
        this.color = color;
    }

    public float[] getColor() {
        return color;
    }
}
