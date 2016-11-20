package cs371m.tardibear.suito.gfx;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kaleb on 11/10/16.
 */


public class Shader {

    private int programHandle;

    private int positionLoc;
    private int colorLoc;
    private int uvLoc;

    private HashMap<Integer, Integer> shaderMap;
    private HashMap<String, Integer> uniformMap;
    private HashMap<String, Integer> attributeMap;

    public Shader() {
        programHandle = GLES20.glCreateProgram();
        uniformMap = new HashMap<>();
        attributeMap = new HashMap<>();
        shaderMap = new HashMap<>();
        positionLoc = uvLoc = colorLoc = -1;
    }

    public Shader attach(int type, String src) {
        Integer v = null;
        try {
            v = Graphics.loadShader(type, src);
        } catch (Exception e) {
            Log.e("shader.attach",  "Could not attach shader");
            e.printStackTrace();
        }
        Integer k = type;
        GLES30.glAttachShader(programHandle, v);
        shaderMap.put(k,v);
        return this;
    }

    public Shader attach(int type, Context context, int resID) {
        Integer v = null;
        try {
            v = Graphics.loadShaderFromRaw(context, type, resID);
        } catch (Exception e) {
            Log.e("shader.attach",  "Could not attach shader");
            e.printStackTrace();
        }
        Integer k = type;
        GLES30.glAttachShader(programHandle, v);
        shaderMap.put(k,v);
        return this;
    }

    public void compile(String[] attributes, String[] uniforms) {

        if(attributes != null) {
            for (int i = 0; i<attributes.length; ++i) {
                GLES30.glBindAttribLocation(programHandle, i, attributes[i]);
            }
        }

        GLES30.glLinkProgram(programHandle);
        if(attributes != null && uniforms != null) {

            for (int i = 0; i<attributes.length; ++i) {
                attributeMap.put(attributes[i],GLES30.glGetAttribLocation(programHandle, attributes[i]));
            }

            for (int i = 0; i<uniforms.length; ++i) {
                uniformMap.put(uniforms[i], GLES30.glGetUniformLocation(programHandle, uniforms[i]));
            }
        }
    }

    public void use() { GLES30.glUseProgram(programHandle);}

    public int getAttribLocation(String str) {
        return attributeMap.get(str);
    }

    public int getUniformbLocation(String str) {
        return uniformMap.get(str);
    }

    public int getPositionHandle() {
        if(positionLoc == -1)
            positionLoc = getAttribLocation("a_position");
        return positionLoc;
    }

    public int getUVHandle() {
        if(uvLoc == -1)
            uvLoc = getAttribLocation("a_uv");
        return uvLoc;
    }

    public int getColorHandle() {
        if(colorLoc == -1)
            colorLoc = getAttribLocation("a_color");
        return colorLoc;
    }

    public int get(String str) {
        Integer i = uniformMap.get(str);
        if( i == null)
        {
            i = attributeMap.get(str);
            if( i == null)
                return -1;
            return i;
        }
        return i;
    }

    public int getProgramHandle() {
        return programHandle;
    }
}
