package org.tardibear.enki;

import android.opengl.GLES20;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {


    private int program;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private FloatBuffer vertexBuffer;
    private ShortBuffer indicesBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw mVertices



    private int [] vaoID = new int[1];
    private int [] vboID = new int [1];
    private int [] eboID = new int [1];

    public Square() {


        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);

        vertexBuffer = ByteBuffer.allocateDirect(squareCoords.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(squareCoords).position(0);
        indicesBuffer = ByteBuffer.allocateDirect(drawOrder.length*2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indicesBuffer.put(drawOrder).position(0);

        program = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);

        GLES20.glGenBuffers(1,vaoID, 0);
        GLES20.glGenBuffers(1,vboID, 0);
        GLES20.glGenBuffers(1,eboID, 0);

        GLES30.glBindVertexArray(vaoID[0]);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboID[0]);
        vertexBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, squareCoords.length*4,
                vertexBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, eboID[0]);
        indicesBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, drawOrder.length*2,
                indicesBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glVertexAttribPointer(0,COORDS_PER_VERTEX,GLES20.GL_FLOAT, false, vertexStride, 0);
        GLES20.glEnableVertexAttribArray(0);
        GLES30.glBindVertexArray(0);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw(float[] color) {
        // Add program to OpenGL ES environment

        GLES30.glBindVertexArray(vaoID[0]);

        GLES20.glUseProgram(program);


        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(program, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,GLES20.GL_UNSIGNED_SHORT,0);

        // Disable vertex array
        GLES30.glBindVertexArray(vaoID[0]);
    }
}