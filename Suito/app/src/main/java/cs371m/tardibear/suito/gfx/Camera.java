package cs371m.tardibear.suito.gfx;
import android.animation.FloatArrayEvaluator;
import android.opengl.Matrix;


/**
 * Created by kaleb on 11/10/16.
 */

public class Camera {

    private static final float ZOOM_NEAR_LIMIT = 0.1f;
    private static final float ZOOM_DELTA = .75f;
    private static final float ZOOM_FAR_LIMIT = 9.0f;

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

    public void zoomIn() {
        mEye[2] -= ZOOM_DELTA;
        if( mEye[2] < ZOOM_NEAR_LIMIT) {
            mEye[2] = ZOOM_NEAR_LIMIT;
        }
    }

    public void zoomOut() {
        mEye[2] += ZOOM_DELTA;
        if( mEye[2] > ZOOM_FAR_LIMIT) {
            mEye[2] = ZOOM_FAR_LIMIT;
        }
    }




    public void translate(float[] p) {
        pos[0] += p[0];
        pos[1] += p[1];
//        pos[2] += p[2];
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
