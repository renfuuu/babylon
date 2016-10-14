package org.koalanis.enki.gfx;

import android.opengl.GLES20;

import java.nio.IntBuffer;
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
        Integer v = Graphics.loadShader(type, src);
        Integer k = type;
        GLES20.glAttachShader(programHandle, v);
        shaderMap.put(k,v);
        return this;
    }

    public void compile(String[] attributes, String[] uniforms) {
        for (int i = 0; i<attributes.length; ++i) {
            GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
        }
        GLES20.glLinkProgram(programHandle);

        for (int i = 0; i<attributes.length; ++i) {
            attributeMap.put(attributes[i],GLES20.glGetAttribLocation(programHandle, attributes[i]));
        }

        for (int i = 0; i<uniforms.length; ++i) {
            uniformMap.put(uniforms[i], GLES20.glGetUniformLocation(programHandle, uniforms[i]));
        }
    }

    public void use() { GLES20.glUseProgram(programHandle);}

    public int getAttribLocation(String str) {
        return attributeMap.get(str);
    }

    public int getUniformbLocation(String str) {
        return uniformMap.get(str);
    }

    public int getPositionHandle() {
        if(positionLoc == -1)
            positionLoc = getAttribLocation("a_Position");
        return positionLoc;
    }

    public int getUVHandle() {
        if(uvLoc == -1)
            uvLoc = getAttribLocation("a_UV");
        return uvLoc;
    }

    public int getColorHandle() {
        if(colorLoc == -1)
            colorLoc = getAttribLocation("a_Color");
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






}
