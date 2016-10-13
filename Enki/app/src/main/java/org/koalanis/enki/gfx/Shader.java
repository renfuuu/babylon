package org.koalanis.enki.gfx;

import android.opengl.GLES20;

import java.nio.IntBuffer;
import java.util.HashMap;

/**
 * Created by kaleb on 11/10/16.
 */


public class Shader {

    private int programHandle;

    private HashMap<Integer, Integer> shaderMap;
    private HashMap<String, Integer> uniformMap;
    private HashMap<String, Integer> attributeMap;

    public Shader() {
        programHandle = GLES20.glCreateProgram();
        uniformMap = new HashMap<>();
        attributeMap = new HashMap<>();
    }

    public Shader attach(int type, String src) {
        Integer v = Graphics.loadShader(type, src);
        Integer k = type;
        GLES20.glAttachShader(programHandle, v);
        shaderMap.put(k,v);
        return this;
    }

    public void compile(String[] attributes) {
        for (int i = 0; i<attributes.length; ++i) {
            GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
        }
        GLES20.glLinkProgram(programHandle);
    }





}
