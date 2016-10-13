package org.koalanis.enki.gfx;

import android.opengl.Matrix;

/**
 * Created by kaleb on 11/10/16.
 */

public class Camera {

    private float[] mEye = new float[3];

    private float[] mLook = new float[3];

    private float[] mUp = new float[3];

    private float[] mView = new float[16];

    public Camera() {
        Matrix.setIdentityM(mView, 0);
    }

    public void setEye(float[] eye) {
        mEye = eye;
    }

    public void setLook(float[] look) {
        mLook = look;
    }

    public void setUp(float[] up) {
        mUp = up;
    }

    public float[] getViewMatrix() {
        Matrix.setLookAtM( mView, 0, mEye[0], mEye[1], mEye[2], mLook[0], mLook[1], mLook[2], mUp[0], mUp[1], mUp[2]);
        return mView;
    }
}
