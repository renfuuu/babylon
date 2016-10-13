package org.koalanis.enki;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * Created by kaleb on 8/10/16.
 */

public class Hexagon {


    private boolean mModelStale = false;
    private float mScale;
    private float[] mPosition = new float[3];
    public static final float[] getUnitHexagon() {
        float s = 1.0f;
        float ns = -1.0f*s;
        float cos_30 = (float)(Math.cos(Math.toRadians(30)));
        float sin_30 = (float)(Math.sin(Math.toRadians(30)));
        return new float[]{
                // fan of hexagon counter clockwise

                0.0f, 0.0f, 0.0f,
                0.0f, s, 0.0f,
                ns*cos_30, s*sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                ns*cos_30, s*sin_30, 0.0f,
                ns*cos_30, ns*sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                ns*cos_30, ns*sin_30, 0.0f,
                0.0f, ns, 0.0f,

                0.0f, 0.0f, 0.0f,
                0.0f, ns, 0.0f,
                s*cos_30, ns*sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                s*cos_30, ns*sin_30, 0.0f,
                s*cos_30, s*sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                s*cos_30, s*sin_30, 0.0f,
                0.f, s, 0.0f,};

    }

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "uniform float uScale"+
                    "uniform vec3 uPos"+
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.\
                    "mat4 model = mat4(1);"+
                    "mat4 trans = mat4(1);"+
                    "mat4 scale = mat4(1);"+
                    "trans[3].xyz = uPos;"+
                    "scale = uScale*scale;"+
                    "model = trans * scale;"+
                    "  gl_Position = uMVPMatrix * model * vPosition;" +
                    "}";

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private final String fragmentShaderCode =
                    "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";



    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    private int uVertexPosition;
    private int uColorPosition;
    private int uPosition;
    private int uScale;

    private final int vertexCount = mVertices.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private int mProgram;
    private float[] mModelMatrix = new float[16];
    private FloatBuffer mVertexBuffer;
    static float mVertices[] = getUnitHexagon();
    private float mColor[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };




    public Hexagon() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                mVertices.length * 4);
        // use th e device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        mVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        mVertexBuffer.put(mVertices);
        // set the buffer to read the first coordinate
        mVertexBuffer.position(0);

        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);


        Matrix.setIdentityM(mModelMatrix, 0);

        // get handle to vertex shader's vPosition member
        uVertexPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        uPosition = GLES20.glGetAttribLocation(mProgram, "uPos");
        uScale = GLES20.glGetAttribLocation(mProgram, "u");

    }


    public void setColor(float[] mColor) {
        this.mColor = mColor;
    }

    public void setScale(float mScale) {
        this.mModelStale = true;
        this.mScale = mScale;
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void setPosition(float x, float y, float z) {
        this.mModelStale = true;
        this.mPosition[0] = x;
        this.mPosition[1] = y;
        this.mPosition[2] = z;
        Matrix.setIdentityM(mModelMatrix, 0);

    }

    public void draw(float[] pMVP, float[] pColor) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);
        if(pColor != null)
            mColor = pColor;

        // Enable a handle to the triangle mVertices
        GLES20.glEnableVertexAttribArray(uVertexPosition);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(uVertexPosition, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);

        // get handle to fragment shader's vColor member
        uColorPosition = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set mColor for drawing the triangle
        GLES20.glUniform4fv(uColorPosition, 1, mColor, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, pMVP, 0);
        GLES20.glUniform1f(uScale, mScale);
        GLES20.glUniform3fv(uPosition, mPosition.length, mPosition,  0);


        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(uVertexPosition);
    }


    public float[] getModelMatrix(float[] pMVP) {

        if(mModelStale)
        {
            mModelStale = false;
            Matrix.translateM(mModelMatrix, 0, mPosition[0], mPosition[1], mPosition[2]);
            Matrix.scaleM(mModelMatrix, 0, mScale, mScale, mScale);
        }
        float[] scratch = new float[pMVP.length];
        Matrix.multiplyMM(scratch, 0, pMVP, 0, mModelMatrix, 0);
        Log.d("mm", Arrays.toString(scratch));


        return scratch;
    }
}