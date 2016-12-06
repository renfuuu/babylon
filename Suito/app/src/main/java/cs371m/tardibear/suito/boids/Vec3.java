package cs371m.tardibear.suito.boids;

import android.opengl.GLES32;

import java.util.Arrays;

/**
 * Created by kaleb on 11/29/2016.
 */

public class Vec3 {
    private float[] con;

    public static Vec3 unitZ() {
        return new Vec3(0,0,1);
    }

    public static Vec3 zero() {
        return new Vec3(0,0,0);
    }

    public Vec3() {
        con = new float[3];
    }
    public Vec3 xy(){
        return new Vec3(con[0], con[1], 0.0f);
    }
    public Vec3(float x, float y, float z) {
        con = new float[] {x,y,z};
    }

    public float[] asArray() {
        return con;
    }

    public Vec3 add(Vec3 other) {
        Vec3 temp = new Vec3();

        for (int i = 0; i < con.length; i++) {
            temp.con[i] = con[i] + other.con[i];
        }
        return temp;
    }

    public Vec3 sub(Vec3 other) {
        Vec3 temp = new Vec3();

        for (int i = 0; i < con.length; i++) {
            temp.con[i] = con[i] - other.con[i];
        }
        return temp;
    }

    public float dot(Vec3 other) {
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
        return "Vec3{" +
                "con=" + Arrays.toString(con) +
                "len=" +len()+
                '}';
    }

    public static Vec3 randomVec3() {
        Vec3 r = new Vec3();
        for (int i = 0; i < r.con.length; i++) {
            r.con[i] = ((float)Math.random());
        }
        return r;
    }

    public void setZ(float z) {
        con[2] = z;
    }

    public void setX(float x) {
        con[0] = x;
    }

    public void setY(float y) {
        con[1] = y;
    }
}
