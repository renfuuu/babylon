package org.tardibear.enki.mkii;

import android.opengl.GLES30;

import org.tardibear.enki.gfx3.Model;
import org.tardibear.enki.gfx3.RenderContext;
import org.tardibear.enki.gfx3.Shader;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by kaleb on 11/8/2016.
 */

class InstancedTarget implements Renderable {


    private final int mLenInstanceData;
    private FloatBuffer mVertices;
    private ShortBuffer mIndices;
    private Shader mShader;
    private RenderContext mRenderContext;
    private Model mModel;
    private int mTexture;

    private int mLenVertices;
    private int mLenIndices;

    private int[] mVAO = new int[1];

    private int[] mVBOs = new int[3];

    final int BYTES_PER_FLOAT = 4;
    final int BYTES_PER_SHORT = 2;

    final int VERTEX_POS_SIZE = 2; // x, y
    final int VERTEX_COLOR_SIZE = 4; // r, g, b, and a
    final int VERTEX_UV_SIZE = 2; // u,v
    final int INSTANCE_DATA_SIZE = 20; // x,y,r,g,b,a

    final int VERTEX_POS_INDX = 0;
    final int VERTEX_COLOR_INDX = 1;
    final int VERTEX_UV_INDX = 2;
    final int INSTANCE_POS_INDX = 3;

    final int VERTEX_STRIDE = (BYTES_PER_FLOAT * (VERTEX_POS_SIZE + VERTEX_COLOR_SIZE + VERTEX_UV_SIZE));
    private float[] mColor;
    private FloatBuffer mInstanceData;
    private int mBindingPoint = 1;

//    //-------------------------------------------------------------------------------------
//        Inspired by combining my renderable code with
//    http://android-developers.blogspot.com/2015/05/game-performance-geometry-instancing.html

    public InstancedTarget(float[] verts, short[] indices, float[] instanceData) {
        mLenIndices = indices.length;
        mLenVertices = verts.length;
        mLenInstanceData = instanceData.length;

        mVertices = ByteBuffer.allocateDirect(verts.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(verts).position(0);
        mIndices = ByteBuffer.allocateDirect(indices.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(indices).position(0);

        mInstanceData = ByteBuffer.allocateDirect(instanceData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mInstanceData.put(instanceData).position(0);

        mModel = new Model();
        mColor = new float[4];
    }

    public void draw(RenderContext renderContext) {

        mShader.use();

        GLES30.glBindVertexArray(mVAO[0]);
        //Texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture);


        GLES30.glDrawElementsInstanced(GLES30.GL_TRIANGLES, mLenIndices, GLES30.GL_UNSIGNED_SHORT, mIndices, mLenInstanceData/INSTANCE_DATA_SIZE);
        GLES30.glBindVertexArray(0);
    }

    public void init() {

        int blockIndex = GLES30.glGetUniformBlockIndex(mShader.getProgramHandle(), "PerInstanceData");
        GLES30.glUniformBlockBinding(mShader.getProgramHandle(), blockIndex, mBindingPoint);


        // Generate VBO Ids and load the VBOs with data
        GLES30.glGenBuffers(3, mVBOs, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOs[0]);

        mVertices.position(0);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mLenVertices * BYTES_PER_FLOAT,
                mVertices, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOs[1]);

        mIndices.position(0);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, BYTES_PER_SHORT * mLenIndices,
                mIndices, GLES30.GL_STATIC_DRAW);


        GLES30.glBindBuffer(GLES30.GL_UNIFORM_BUFFER, mVBOs[2]);

        mInstanceData.position(0);
        GLES30.glBufferData(GLES30.GL_UNIFORM_BUFFER, BYTES_PER_FLOAT * mLenInstanceData,
                mInstanceData, GLES30.GL_DYNAMIC_DRAW);

        // Generate VAO Id
        GLES30.glGenVertexArrays(1, mVAO, 0);

        // Bind the VAO and then setup the vertex
        // attributes
        GLES30.glBindVertexArray(mVAO[0]);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOs[0]);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOs[1]);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOs[2]);

        GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX);
        GLES30.glEnableVertexAttribArray(VERTEX_COLOR_INDX);
        GLES30.glEnableVertexAttribArray(VERTEX_UV_INDX);

        GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                0);

        GLES30.glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                (VERTEX_POS_SIZE * 4));

        GLES30.glVertexAttribPointer (VERTEX_UV_INDX, VERTEX_UV_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE, (VERTEX_POS_SIZE+VERTEX_COLOR_SIZE)*BYTES_PER_FLOAT);


        // Reset to the default VAO
        GLES30.glBindVertexArray(0);
    }

    // where we update instance data
    public void update() {
        GLES30.glBindBuffer(GLES30.GL_UNIFORM_BUFFER, mVBOs[2]);
        Buffer buff = GLES30.glMapBufferRange(GLES30.GL_UNIFORM_BUFFER,0,mLenInstanceData, GLES30.GL_MAP_WRITE_BIT | GLES30.GL_MAP_INVALIDATE_RANGE_BIT);

        GLES30.glUnmapBuffer(GLES30.GL_UNIFORM_BUFFER);
    }

    //--------------------------------------------------------------------------------------

    //------GETTERS AND SETTERS AND OTHERS--------------------------------------------------

    //--------------------------------------------------------------------------------------



    public Shader Shader() {
        return mShader;
    }

    public void attachShader(Shader mShader) {
        this.mShader = mShader;
    }

    public RenderContext getRenderContext() {
        return mRenderContext;
    }

    public Model getModel() {
        return mModel;
    }

    public void setModel(Model mModel) {
        this.mModel = mModel;
    }

    public int getTexture() {
        return mTexture;
    }

    public void setTexture(int mTexture) {
        this.mTexture = mTexture;
    }

    public void setColor(float[] color) {
        mColor = color;
    }
}