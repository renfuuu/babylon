package org.tardibear.enki.gfx3;

import org.tardibear.enki.mkii.RenderTarget;
import org.tardibear.enki.mkii.Renderable;

/**
 * Created by kaleb on 28/10/16.
 */

public class Sprite extends RenderTarget implements Renderable{
    float width;
    float height;

    int texture;

    public Sprite() {
        super(Graphics.getUnitTexturedSquareVertices(), Graphics.getUnitTexturedSquareIndices());
    }

}
