package cs371m.tardibear.suito.gfx;

import cs371m.tardibear.suito.boids.GLCallback;
import cs371m.tardibear.suito.boids.RenderTarget;
import cs371m.tardibear.suito.boids.Renderable;

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
