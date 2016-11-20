package org.tardibear.enki.gfx3;

import org.tardibear.enki.mkii.GLCallback;
import org.tardibear.enki.mkii.RenderTarget;
import org.tardibear.enki.mkii.Renderable;

/**
 * Created by kaleb on 28/10/16.
 */

public class Sprite extends RenderTarget implements GLCallback{
    float width;
    float height;

    int texture;

    public Sprite() {
        super(Graphics.getUnitTexturedSquareVertices(), Graphics.getUnitTexturedSquareIndices());
    }

    @Override
    public void preDrawFrame(RenderContext rc) {

    }



}
