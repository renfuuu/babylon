
package cs371m.tardibear.suito.gles;

import android.opengl.GLES20;
import android.opengl.GLES30;

import cs371m.tardibear.suito.gfx.Model;
import cs371m.tardibear.suito.gfx.Graphics;
import cs371m.tardibear.suito.gfx.Obj;
import cs371m.tardibear.suito.gfx.RenderContext;
import cs371m.tardibear.suito.gfx.Shader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


public class RenderTarget extends Renderable{

    private FloatBuffer mVertices;
    private ShortBuffer mIndices;
    private Shader mShader;
    private RenderContext mRenderContext;
    private Model mModel;
    private int mTexture;

    private int mNumVertices;
    private int mNumIndices;

    private int [] mVAO = new int[1];

    private int [] mVBOs = new int[2];

    final int BYTES_PER_FLOAT = 4;

    final int VERTEX_POS_SIZE   = 3; // x, y and z
    final int VERTEX_COLOR_SIZE = 4; // r, g, b, and a
    final int VERTEX_UV_SIZE = 2; // u,v

    final int VERTEX_POS_INDX   = 0;
    final int VERTEX_COLOR_INDX = 1;
    final int VERTEX_UV_INDX = 2;

    final int VERTEX_STRIDE     =  ( BYTES_PER_FLOAT * ( VERTEX_POS_SIZE + VERTEX_COLOR_SIZE + VERTEX_UV_INDX) );
    private float[] mColor;

    //-------------------------------------------------------------------------------------


    public RenderTarget(float[] verts, short[] indices) {
        mNumIndices = indices.length;
        mNumVertices = verts.length;
        mVertices = ByteBuffer.allocateDirect( verts.length * 4 )
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(verts).position(0);

        mIndices = ByteBuffer.allocateDirect( indices.length * 2 )
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(indices).position(0);

        mModel = new Model();
        mColor = new float[4];
    }

    public RenderTarget(Obj obj){
        mNumIndices = obj.indices.length;
        mNumVertices = obj.vertices.length;
        mVertices = ByteBuffer.allocateDirect( obj.vertices.length * 4 )
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(obj.vertices).position(0);

        mIndices = ByteBuffer.allocateDirect( obj.indices.length * 2 )
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(obj.indices).position(0);

        mModel = new Model();
        mColor = new float[4];

    }

    public void init() {
        // Generate VBO Ids and load the VBOs with data
        GLES30.glGenBuffers ( 2, mVBOs, 0 );

        GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOs[0] );

        mVertices.position ( 0 );
        GLES30.glBufferData ( GLES30.GL_ARRAY_BUFFER, mNumVertices * 4,
                mVertices, GLES30.GL_STATIC_DRAW );

        GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOs[1] );

        mIndices.position ( 0 );
        GLES30.glBufferData ( GLES30.GL_ELEMENT_ARRAY_BUFFER, 2 * mNumIndices,
                mIndices, GLES30.GL_STATIC_DRAW );

        // Generate VAO Id
        GLES30.glGenVertexArrays ( 1, mVAO, 0 );

        // Bind the VAO and then setup the vertex
        // attributes
        GLES30.glBindVertexArray ( mVAO[0] );

        GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOs[0] );
        GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOs[1] );

        GLES30.glEnableVertexAttribArray ( VERTEX_POS_INDX );
        GLES30.glEnableVertexAttribArray ( VERTEX_COLOR_INDX );
        GLES30.glEnableVertexAttribArray( VERTEX_UV_INDX );

        GLES30.glVertexAttribPointer ( VERTEX_POS_INDX, VERTEX_POS_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                0 );

        GLES30.glVertexAttribPointer ( VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                ( VERTEX_POS_SIZE * 4 ) );

        GLES30.glVertexAttribPointer (VERTEX_UV_INDX, VERTEX_UV_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE, (VERTEX_POS_SIZE+VERTEX_COLOR_SIZE)*BYTES_PER_FLOAT);

        // Reset to the default VAO
        GLES30.glBindVertexArray ( 0 );
    }


    @Override
    public void preDrawFrame(RenderContext rc) {
//        GLES30.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//        GLES30.glEnable(GL10.GL_BLEND);
    }

    @Override
    public void postDrawFrame(RenderContext rc) {

    }

    public void onDrawFrame(RenderContext renderContext) {

        mShader.use();

        GLES30.glBindVertexArray(mVAO[0]);
        //Texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture);


        //Uniforms [Color, Perspective, View, Model]
        GLES30.glUniformMatrix4fv(mShader.get("u_persp"),
                            1,
                            false,
                            renderContext.getPerspective(),
                            0);

        GLES30.glUniformMatrix4fv(mShader.get("u_view"),
                            1,
                            false,
                            renderContext.getCamera().getViewMatrix(),
                            0);

        GLES30.glUniformMatrix4fv(mShader.get("u_model"),
                1,
                false,
                mModel.getModelMatrix(),
                0);

        GLES30.glUniform4fv(mShader.get("u_color"), 1, mColor, 0);

        GLES30.glDrawElements( GLES30.GL_TRIANGLES, mNumIndices, GLES30.GL_UNSIGNED_SHORT, 0 );

        GLES30.glBindVertexArray(0);
    }



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