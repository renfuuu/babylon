package cs371m.tardibear.suito.gfx;

import cs371m.tardibear.suito.gles.GLCallback;
import cs371m.tardibear.suito.gles.RenderTarget;
import cs371m.tardibear.suito.gles.Renderable;

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
