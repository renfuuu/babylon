package org.koalanis.enki.gfx;

import android.opengl.Matrix;

import org.koalanis.enki.MainRenderer;
import org.koalanis.enki.SimpleRenderer;

import java.util.Arrays;

/**
 * Created by kaleb on 11/10/16.
 */

public class Camera {

    private float[] pos = new float[3];

    private float[] mEye = new float[3];

    private float[] mCenter= new float[3];

    private float[] mLook = new float[3];

    private float[] mUp = new float[3];

    private float[] mView = new float[16];

    float yaw, pitch, lastx, lasty, fov;

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


    public void setCenter(float x,float y,float z) {
        mCenter[0]=x;mCenter[1]=y;mCenter[2]=z;
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




    public void translate(float[] p) {
        pos[0] += p[0];
        pos[1] += p[1];
//        pos[2] += p[2];
    }

    public float[] alignWithClick(SimpleRenderer mRenderer, float  width, float height) {
        float touch_x= width;
        float touch_y = height;
        float[] ppT = new float[4];
        float ndc_x = (2.0f*touch_x)/mRenderer.getRenderContext().getWidth() - 1.0f;
        float ndc_y = 1.0f - (2.0f*touch_y)/mRenderer.getRenderContext().getHeight();

        float[] pT =  {ndc_x,ndc_y,-1.0f, 1.0f};
        mRenderer.getRenderContext().update();
        float[] persp = mRenderer.getRenderContext().getPerspective();
        float[] iM = new float[16];
        Matrix.invertM(iM, 0 ,persp,0);
        Matrix.multiplyMV(ppT, 0, iM, 0, pT, 0);
        float[] temp = new float[16];
        ppT[2] = -1.0f;
        ppT[3] = 0.0f;
        float[] world_ray = new float[3];
        Matrix.invertM(temp, 0, getViewMatrix(), 0);
        Matrix.multiplyMV(pT, 0, temp, 0, ppT, 0);
        world_ray[0] = pT[0];
        world_ray[1] = pT[1];
        world_ray[2] = pT[2];
        float scalar = Matrix.length(world_ray[0], world_ray[1], world_ray[2]);
        world_ray[0] = world_ray[0]/scalar;
        world_ray[1] = world_ray[1]/scalar;
        world_ray[2] = world_ray[2]/scalar;
        float[] pp = {0.0f,0.0f,0.0f};
        float[] l0 = mEye;
        float[] l = world_ray;
        float[] n = {0,0,1.0f};

        float d = computeIntersect(pp,l0, n, l);
        float d1 = computeIntersect(pp, mEye, n, mLook);


        if(d != -1.0f) {
            float[] p0 = {mEye[0]+d*world_ray[0],mEye[1]+d*world_ray[1],mEye[2]+d*world_ray[2]};
            float[] p1 = {mEye[0]+d1*mLook[0],mEye[1]+d1*mLook[1],mEye[2]+d1*mLook[2]};
            float[] delta = {p0[0] - p1[0], p0[1] - p1[1], p0[2] - p1[2]};

//        float[] displacement = {world_ray[0] - mLook[0], world_ray[1] - mLook[1], world_ray[2] - mLook[2]};
            // let ray = new eye

            setEye(mEye[0]+delta[0], mEye[1]+delta[1], mEye[2]);
            createViewMatrix();
        }

        return mEye;

    }

    private float computeIntersect(float[] p0, float[] l0, float[] n, float[] l) {
        float[] temp = new float[p0.length];
        for (int i = 0; i < p0.length; i++) {
            temp[i] = p0[i] - l0[i];
        }
        float num = 0.0f;
        for (int i = 0; i < temp.length; i++) {
            num += temp[i]*n[i];
        }
        float den = 0.0f;
        for (int i = 0; i < l.length; i++) {
            den += l[i]*n[i];
        }

        if(den == 0.0f)
            return -1.0f;

        return num/den;


    }

    public void handleSwipe(RenderContext mRenderer, float x, float y, float prevX, float prevY, float size) {


        float n_x = (2.0f*x)/mRenderer.getWidth() - 1.0f;
        float n_y = 1.0f - (2.0f*y)/mRenderer.getHeight();

        float n_prevx = (2.0f*prevX)/mRenderer.getWidth() - 1.0f;
        float n_prevy = 1.0f - (2.0f*prevY)/mRenderer.getHeight();


        float deltaX = n_x - n_prevx;
        float deltaY = n_y - n_prevy;
        float ex = mEye[0] + deltaX*size;
        float ey = mEye[1] + deltaY*size;

        float cx = mLook[0] + deltaX*size;
        float cy = mLook[1] + deltaY*size;

        setEye(ex, ey, mEye[2]);
        setLook(cx, cy, mLook[2]);

        mView = createViewMatrix();
    }

    public float[] createViewMatrix() {
        Matrix.setLookAtM( mView, 0, mEye[0], mEye[1], mEye[2], mLook[0], mLook[1], mLook[2], mUp[0], mUp[1], mUp[2]);
        return mView;
    }

    public float[] getViewMatrix() {
        return mView;
    }
}
