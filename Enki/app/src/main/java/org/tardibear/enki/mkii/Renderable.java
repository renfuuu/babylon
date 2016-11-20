package org.tardibear.enki.mkii;

import org.tardibear.enki.gfx3.Model;
import org.tardibear.enki.gfx3.RenderContext;
import org.tardibear.enki.gfx3.Shader;

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
