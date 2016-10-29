package org.tardibear.enki.gfx;

/**
 * Created by kaleb on 28/10/16.
 */

public class Sprite extends Renderable {
    float width;
    float height;

    int texture;

    public Sprite() {
        super(Graphics.getUnitTexturedColoredSquare());
    }
}
