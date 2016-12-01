package cs371m.tardibear.suito.boids;

import android.opengl.GLES32;

import java.util.Arrays;

/**
 * Created by kaleb on 11/29/2016.
 */

public class Vec4 {
    private float[] con;


    public Vec4() {
        con = new float[4];
    }

    public Vec4(Vec3 vec3) {
        con = new float[4];
        float[] o = vec3.asArray();
        for (int i = 0; i < o.length; i++) {
            con[i] = o[i];
        }
    }

    public Vec4(float x, float y, float z, float w) {
        con = new float[] {x,y,z,w};
    }

    public float[] asArray() {
        return con;
    }

    public Vec4 add(Vec4 other) {
        Vec4 temp = new Vec4();

        for (int i = 0; i < con.length; i++) {
            temp.con[i] = con[i] + other.con[i];
        }
        return temp;
    }

    public Vec4 sub(Vec4 other) {
        Vec4 temp = new Vec4();

        for (int i = 0; i < con.length; i++) {
            temp.con[i] = con[i] - other.con[i];
        }
        return temp;
    }

    public float dot(Vec4 other) {
        float t = 0.0f;
        for (int i = 0; i < con.length; i++) {
            t += con[i]*other.con[i];
        }
        return t;
    }

    public float len() {
        return (float)(Math.sqrt(this.dot(this)));
    }

    public void scale(float s) {
        for (int i = 0; i < con.length; i++) {
            con[i] *= s;
        }
    }

    public void normalize() {
        this.scale(1.0f/len());
    }

    @Override
    public String toString() {
        return "Vec4{" +
                "con=" + Arrays.toString(con) +
                "len=" +len()+
                '}';
    }
}
