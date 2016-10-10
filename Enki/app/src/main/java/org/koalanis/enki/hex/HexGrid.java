package org.koalanis.enki.hex;

import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import static org.koalanis.enki.hex.HexTile.*;

/**
 * Created by kaleb on 8/10/16.
 */
public class HexGrid {

    public HashMap<Hex.Offset,HexTile> map;

    private float size;

    public HexGrid(int r, int c, float size) {
        this.size = size;
        this.map = new HashMap<>();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                map.put(Hex.I().makeOffset(i,j), new HexTile(this,i,j));
            }
        }
    }

    public HexTile get(int r, int c) {
        return map.get(Hex.I().makeOffset(r,c));
    }

    public float[] hexToPixel(HexTile hex) {
        float x = (float) (size * Math.sqrt(hex.col + 0.5 * (hex.row & 1)));
        float y = (float) (size* 3.0 / 2.0 * hex.row);
        float[] pos = new float[] {x,y,0.f};
//        Log.d("pos", ""+ Arrays.toString(pos));
        return pos;
    }
}
