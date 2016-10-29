package org.koalanis.enki;


import org.koalanis.enki.gfx.Graphics;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kaleb on 11/10/16.
 */

public class MainRenderer implements GLSurfaceView.Renderer {

    private final FloatBuffer triangleVerts;

    private float x,y;


    final String vertexShader = "uniform mat4 u_MVPMatrix;"
                    +"attribute vec3 a_Position;"
                    +"attribute vec4 a_Color;"
                    +"attribute vec2 a_UV;"
                    +"varying vec4 v_Color;"
                    +"varying vec2 v_UV;"
                    +"void main() {"
                    +" v_Color = a_Color;"
                    +" v_UV = a_UV;"
                    +" gl_Position = u_MVPMatrix * vec4(a_Position, 1.0f);"
                    +"}";

    final private int BYTES_PER_FLOAT = 4;

    final String fragmentShader ="precision mediump float;"
                                +"varying vec4 v_Color;"
                                +"uniform sampler2D u_Texture;"
                                +"varying vec2 v_UV;"
                                +"void main() {"
                                +" gl_FragColor = v_Color* texture2D(u_Texture, v_UV);"
                                +"}";

    private float[] mViewMatrix = new float[16];

    private int programHandle;
    private int vertexShaderHandle;
    private int fragShaderHandle;
    private Context parentContext;

    public MainRenderer(Context context) {
        parentContext = context;
        final float[] data = {
                -0.5f, -0.25f, 0.f,
                1.0f, 0.0f, 0.0f, 1.0f,
                0.f, 0.f,
                0.5f, -0.25f, 0.f,
                0.0f, 0.0f, 1.0f, 1.0f,
                1.f, 0.f,
                0.0f, 0.559f, 0.f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.5f,1.0f
        };

        triangleVerts = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        triangleVerts.put(data).position(0);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, y, 0.f);
    }

    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mUVHandle;
    private int mTextureUniformHandle;
    private int mTextureDataHandle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,0.5f);

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;


        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        vertexShaderHandle = Graphics.loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        fragShaderHandle = Graphics.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        programHandle = GLES20.glCreateProgram();
        if(programHandle != 0)
        {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragShaderHandle);
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            GLES20.glBindAttribLocation(programHandle, 2, "a_UV");
            GLES20.glLinkProgram(programHandle);
        }

        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        mUVHandle = GLES20.glGetAttribLocation(programHandle, "a_UV");
        mTextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");

        mTextureDataHandle = Graphics.loadTexture(parentContext, R.drawable.smiley_face);

    }

    private float[] mProjectionMatrix = new float[16];

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width, height);
        final float ratio = (float) width/height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.f;
        final float far = 10.0f;
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private float[] mMVPMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private final int mStrideBytes = 9  * BYTES_PER_FLOAT;
    private final int mPositionOffset = 0;
    private final int mPositionDataSize  = 3;
    private final int mColorOffset = 3;
    private final int mColorDataSize = 4;
    private final int mUVOffset = 7;
    private final int mUVDataSize = 2;

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(programHandle);

        triangleVerts.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, mStrideBytes, triangleVerts);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        triangleVerts.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, mStrideBytes, triangleVerts);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        triangleVerts.position(mUVOffset);
        GLES20.glVertexAttribPointer(mUVHandle, mUVDataSize, GLES20.GL_FLOAT, false, mStrideBytes, triangleVerts);
        GLES20.glEnableVertexAttribArray(mUVHandle);

        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
        GLES20.glDisableVertexAttribArray(mUVHandle);
    }

    public void perturbTriangle() {

        x = x+(float)(Math.random())*0.1f;
        y = y+(float)(Math.random())*0.1f;


        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, y, 0.f);

    }
}
