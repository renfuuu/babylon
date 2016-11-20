package org.tardibear.enki.mkii;

import org.tardibear.enki.gfx3.RenderContext;
import org.tardibear.enki.gfx3.Shader;

/**
 * Created by kaleb on 11/20/2016.
 */

public interface GLCallback {

    void init();

    void preDrawFrame(RenderContext rc);

    void postDrawFrame(RenderContext rc);

    void onDrawFrame(RenderContext rc);

    void attachShader(Shader shader);
}
