package org.tardibear.enki;

/**
 * Created by kaleb on 13/10/16.
 */

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import org.tardibear.enki.gfx.Camera;
import org.tardibear.enki.gfx.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.tardibear.enki.gfx.Model;
import org.tardibear.enki.gfx.RenderContext;
import org.tardibear.enki.gfx.Renderable;
import org.tardibear.enki.gfx.Shader;
import org.tardibear.enki.gfx.Sprite;
import org.tardibear.enki.hex.HexGrid;
import org.tardibear.enki.hex.HexTile;

import java.util.ArrayList;

public class SimpleRenderer implements GLSurfaceView.Renderer {

    private RenderContext renderContext;
    private Renderable renderable;
    private Context parentContext;
    private Shader shader;

    private float[] data;
    private HexGrid grid;

    private boolean randomColorVertex = false;
    private boolean textured = false;
    private int mTextureDataHandle1;

    private final int HEX_ROWS = 10;
    private final int HEX_COLS = 10;

    public HexGrid getGrid() {
        return grid;
    }

    public SimpleRenderer(Context context) {

        grid = new HexGrid(HEX_ROWS, HEX_COLS, 1.0f);
        parentContext = context;
        renderContext = new RenderContext();
        data = new float[18*Graphics.VERTEX_DATA_SIZE];
        ArrayList<Float> temp = new ArrayList<>();
        float[] hex = Graphics.getUnitHexagon();
        int size = hex.length;
        int i= 0;
        while(i != size/3) {
            temp.add(hex[3*i]);
            temp.add(hex[3*i+1]);
            temp.add(hex[3*i+2]);

            if(randomColorVertex) {
                temp.add((float)Math.random());
                temp.add((float)Math.random());
                temp.add((float)Math.random());
            }
            else {
                temp.add(1.0f);
                temp.add(1.0f);
                temp.add(1.0f);
                temp.add(1.0f);
            }

            if(hex[3*i] == hex[3*i+1]) {
                temp.add(0.5f);
                temp.add(0.f);
            }
            else {
                if(textured) {
                    if(hex[3*i] < hex[3*i+1]) {
                        temp.add(0.0f);
                        temp.add(1.f);
                    }
                    else{
                        temp.add(1.f);
                        temp.add(1.f);
                    }
                }
                else {
                    temp.add(0.0f);
                    temp.add(0.0f);
                }
            }
            i+=1;
        }

        data = new float[temp.size()];
        for (int j = 0; j < temp.size(); j++) {
            data[j] = temp.get(j);
        }

    }

    public RenderContext getRenderContext() {
        return renderContext;
    }

    private int mTextureDataHandle;


    /**
     * @param gl
     * @param config
     *
     * The GLSurfaceView calls this function from its renderer when it is created.
     * OpenGL context is created before this function is called so any OpenGLES API
     * outside of callbacks after this function will result in error.
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,0.5f);

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;


        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        renderContext.setCamera(new Camera());
        renderContext.getCamera().setEye(eyeX, eyeY, eyeZ);
        renderContext.getCamera().setLook(lookX, lookY, lookZ);
        renderContext.getCamera().setUp(upX, upY, upZ);
        renderContext.getCamera().createViewMatrix();
        renderable = new Renderable(data);

        shader = new Shader();

        shader.attach(GLES20.GL_VERTEX_SHADER, Graphics.vertexShader);
        shader.attach(GLES20.GL_FRAGMENT_SHADER, Graphics.fragmentShader);

        String[] attrs = {"a_Position", "a_Color", "a_UV"};
        String[] unis = {"u_View", "u_Persp", "u_Model", "u_Texture", "u_Color"};

        shader.compile(attrs,unis);

        // texture id
        mTextureDataHandle = Graphics.loadTexture(parentContext, R.drawable.smiley_face);
        mTextureDataHandle1 = Graphics.loadTexture(parentContext, R.drawable.character_ak47_1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width, height);
        renderContext.setWidth(width);
        renderContext.setHeight(height);
        renderContext.update();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        for (HexTile hexTile:
                grid.map.values()) {
            float[] pos = grid.hexToPixel(hexTile);
            Model t = new Model();
            t.setTranslate(pos[0],pos[1],0.0f);
            t.setScale(.25f);
            t.createModelMatrix();
            renderable.draw(shader, mTextureDataHandle, renderContext,t, hexTile.getColor());

        }
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GL10.GL_BLEND);
        new Sprite().draw(shader, mTextureDataHandle1, renderContext, null, new float[] {1.0f,1.f,1.f,1.0f});
    }

    public void perturbTriangle() {

        float e = (float)(1.0f - Math.random())*.1f;
        renderable.getModel().translate(e,e,0.0f);

    }
}
