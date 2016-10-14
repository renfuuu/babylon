package org.koalanis.enki;

/**
 * Created by kaleb on 13/10/16.
 */

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import org.koalanis.enki.gfx.Camera;
import org.koalanis.enki.gfx.Graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.koalanis.enki.gfx.RenderContext;
import org.koalanis.enki.gfx.Renderable;
import org.koalanis.enki.gfx.Shader;

import java.util.ArrayList;

/**
 * Created by kaleb on 11/10/16.
 */

public class SimpleRenderer implements GLSurfaceView.Renderer {

    private RenderContext renderContext;
    private Renderable renderable;
    private Context parentContext;
    private Shader shader;

    private float[] data;



    public SimpleRenderer(Context context) {
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

            temp.add((float)Math.random());
            temp.add((float)Math.random());
            temp.add((float)Math.random());
            temp.add(1.0f);

            if(hex[3*i] == hex[3*i+1]) {
                temp.add(0.5f);
                temp.add(0.f);
            }
            else {
                if(hex[3*i] < hex[3*i+1]) {
                    temp.add(0.0f);
                    temp.add(1.f);
                }
                else{
                    temp.add(1.f);
                    temp.add(1.f);
                }
            }
            i+=1;
        }

        data = new float[temp.size()];
        for (int j = 0; j < temp.size(); j++) {
            data[j] = temp.get(j);
        }

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

        Camera cam = new Camera();

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;
        cam.setEye(eyeX, eyeY, eyeZ);

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;
        cam.setLook(lookX, lookY, lookZ);


        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;
        cam.setUp(upX, upY, upZ);

        renderContext.setCamera(cam);
        renderable = new Renderable(data);

        shader = new Shader();

        shader.attach(GLES20.GL_VERTEX_SHADER, Graphics.vertexShader);
        shader.attach(GLES20.GL_FRAGMENT_SHADER, Graphics.fragmentShader);

        String[] attrs = {"a_Position", "a_Color", "a_UV"};
        String[] unis = {"u_View", "u_Persp", "u_Model", "u_Texture"};


        shader.compile(attrs,unis);

        // texture id
        mTextureDataHandle = Graphics.loadTexture(parentContext, R.drawable.smiley_face);
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

        renderable.draw(shader, mTextureDataHandle, renderContext);
    }

    public void perturbTriangle() {

        float e = (float)(1.0f - Math.random())*.1f;
        renderable.getModel().translate(e,e,0.0f);

    }
}
