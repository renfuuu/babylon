package cs371m.tardibear.suito.gles;
import cs371m.tardibear.suito.gfx.RenderContext;
import cs371m.tardibear.suito.gfx.Shader;

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
