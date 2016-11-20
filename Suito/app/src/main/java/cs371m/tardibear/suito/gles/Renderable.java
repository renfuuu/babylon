package cs371m.tardibear.suito.gles;

import cs371m.tardibear.suito.gfx.Model;
import cs371m.tardibear.suito.gfx.RenderContext;
import cs371m.tardibear.suito.gfx.Shader;

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
