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


    public void setEye(float x,float y,float z) {
        mEye[0]=x;mEye[1]=y;mEye[2]=z;
    }

    public void setLook(float x,float y,float z) {
        mLook[0]=x;mLook[1]=y;mLook[2]=z;
    }

    public void setUp(float x,float y,float z) {
        mUp[0]=x;mUp[1]=y;mUp[2]=z;
    }


    public float[] getViewMatrix() {
        Matrix.setLookAtM( mView, 0, mEye[0], mEye[1], mEye[2], mLook[0], mLook[1], mLook[2], mUp[0], mUp[1], mUp[2]);
        return mView;
    }
}
