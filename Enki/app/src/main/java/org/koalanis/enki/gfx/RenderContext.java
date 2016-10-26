package org.koalanis.enki.gfx;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by kaleb on 13/10/16.
 */

public class RenderContext {

    private int width;
    private int height;
    private Camera camera;

    private float[] perspective = new float[16];

    public RenderContext() {
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Camera getCamera() {
        return camera;
    }

    public float[] getPerspective() {
        return perspective;
    }

    public void update() {
        final float ratio = (float) width/height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = -1.f;
        final float far = 1.0f;



        Matrix.orthoM(perspective, 0, left, right, bottom, top, near, far);

    }
}
