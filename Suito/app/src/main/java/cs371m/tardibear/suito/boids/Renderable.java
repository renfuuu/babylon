package cs371m.tardibear.suito.boids;

import cs371m.tardibear.suito.gfx.RenderContext;

/**
 * Created by kaleb on 11/7/2016.
 */

public abstract class Renderable implements GLCallback {

    public interface RenderableCallback {

    }

    public void draw(RenderContext rc) {
        preDrawFrame(rc);
        onDrawFrame(rc);
        postDrawFrame(rc);
    }


}
