package cs371m.tardibear.suito.gfx;

import android.opengl.Matrix;

/**
 * Created by kaleb on 11/10/16.
 */
public class Model {

    private float[] modelMatrix = new float[16];
    private float[] translate = new float[16];
    private float[] scale = new float[16];
    private float[] rotation = new float[16];



    public Model() {
        Matrix.setIdentityM(translate,0);
        Matrix.setIdentityM(scale,0);
        Matrix.setIdentityM(rotation,0);
        Matrix.setIdentityM(modelMatrix,0);

    }

    public void setTranslate(float x, float y, float z) {
        Matrix.setIdentityM(translate,0);
        translate(x,y,z);
    }

    public float[] getTranslate() {
        return translate;
    }

    public void translate(float x, float y, float z) {
        Matrix.translateM(translate, 0, x, y, z);
    }

    public void setScale(float s) {
        Matrix.setIdentityM(scale,0);
        scale(s);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(scale, 0, x,y,z);
    }

    public void scale(float s) {
        Matrix.scaleM(scale, 0, s,s,s);
    }

    public void rotate(float theta) {
        Matrix.rotateM(rotation, 0, theta, 0.f,0.f,1.f);
    }


    //need to optimize
    public float[] createModelMatrix() {
        Matrix.multiplyMM(modelMatrix, 0, rotation, 0, translate, 0);
        Matrix.multiplyMM(modelMatrix, 0, scale, 0, modelMatrix, 0);
        return modelMatrix;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }



}
