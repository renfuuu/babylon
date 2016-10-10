package org.koalanis.enki;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import org.koalanis.enki.hex.HexGrid;
import org.koalanis.enki.hex.HexTile;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kaleb on 6/10/16.
 */
public class GLRenderer implements GLSurfaceView.Renderer {


    private Square square;
    private Triangle triangle;
    private Hexagon hexagon;
    private Context parent;
    private HexGrid grid;


    public GLRenderer(Context c) {
        parent = c;
        grid = new HexGrid(100,50,.1f);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = ((float) width )/ height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        hexagon = new Hexagon();
        hexagon.setScale(1.0f);
//        hexagon.setPosition(.5f ,.5f, 0);
        Log.d("onSurfaceCreate", "hello");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float[] color = new float[] {1.0f,0.0f,0.0f, 1.0f};

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        // Draw shape

        for (HexTile t:
             grid.map.values()) {
            float[] pos = grid.hexToPixel(t);

            hexagon.setPosition(pos[0],pos[1],pos[2]);
//            randomColor(color);
            hexagon.draw(mMVPMatrix, color);
        }
    }

    private void randomColor(float[] color) {
        color[0] = (float)Math.random();
        color[1] = (float)Math.random();
        color[2] = (float)Math.random();
    }
}