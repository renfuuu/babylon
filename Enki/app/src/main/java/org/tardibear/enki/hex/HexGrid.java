package org.tardibear.enki.hex;

import java.util.HashMap;

/**
 * Created by kaleb on 8/10/16.
 */
public class HexGrid {

    public HashMap<Hex.Offset,HexTile> map;

    // side length of a hex
    private float size;

    // height with respect to vertical orientated hexes
    private float height;

    // width with respect to vertical orientated hexes
    private float width;


    public HexGrid(int r, int c, float size) {
        this.size = size;
        this.map = new HashMap<>();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {

                float[] color = new float[4];
                color[0] = (float)Math.random();
                color[1] = (float)Math.random();
                color[2] = (float)Math.random();
                color[3] = 1.0f;
                map.put(Hex.I().makeOffset(i,j), new HexTile(this,i,j,color));
            }
        }
        height = (2*this.size);
        width = (float) (height*Math.sqrt(3.0)/2.0f);


    }

    public float getSize() {
        return size;
    }

    public HexTile get(int r, int c) {
        return map.get(Hex.I().makeOffset(r,c));
    }

    public float[] hexToPixel(HexTile hex) {
        float x,y = 0.0f;

        x = hex.col*width;
        if(hex.row %2 != 0) {
            x += width/2.0f;
        }
        y = hex.row*height*3.f/4.f;
//
//        float x = (float) (size * Math.sqrt(hex.col + 0.5 * (hex.row & 1)));
//        float y = (float) (size* 3.0 / 2.0 * hex.row);
        float[] pos = new float[] {x,y,0.0f};
        return pos;
    }
}
