//

package org.tardibear.enki.mkii;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import org.tardibear.enki.gfx3.Graphics;
import org.tardibear.enki.gfx3.Shader;

import static org.tardibear.enki.gfx3.Graphics.fShaderStr;

public class VAORenderable implements GLSurfaceView.Renderer{

    ///
    // Constructor
    //
    public VAORenderable ( Context context )
    {
        mVertices = ByteBuffer.allocateDirect ( mVerticesData.length * 4 )
                .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
        mVertices.put ( mVerticesData ).position ( 0 );

        mIndices = ByteBuffer.allocateDirect ( mIndicesData.length * 2 )
                .order ( ByteOrder.nativeOrder() ).asShortBuffer();
        mIndices.put ( mIndicesData ).position ( 0 );
    }

    ///
    // Initialize the shader and program object
    //
    public void onSurfaceCreated ( GL10 glUnused, EGLConfig config )
    {

        // Load the shaders and get a linked program object
        Shader shader = new Shader().attach(GLES30.GL_VERTEX_SHADER, Graphics.vMinShaderStr).attach(GLES30.GL_FRAGMENT_SHADER, Graphics.fMinShaderStr);
        shader.compile(null, null);
        mProgramObject = shader.getProgramHandle();

        // Generate VBO Ids and load the VBOs with data
        GLES30.glGenBuffers ( 2, mVBOIds, 0 );

        GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[0] );

        mVertices.position ( 0 );
        GLES30.glBufferData ( GLES30.GL_ARRAY_BUFFER, mVerticesData.length * 4,
                mVertices, GLES30.GL_STATIC_DRAW );

        GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1] );

        mIndices.position ( 0 );
        GLES30.glBufferData ( GLES30.GL_ELEMENT_ARRAY_BUFFER, 2 * mIndicesData.length,
                mIndices, GLES30.GL_STATIC_DRAW );

        // Generate VAO Id
        GLES30.glGenVertexArrays ( 1, mVAOId, 0 );

        // Bind the VAO and then setup the vertex
        // attributes
        GLES30.glBindVertexArray ( mVAOId[0] );

        GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[0] );
        GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1] );

        GLES30.glEnableVertexAttribArray ( VERTEX_POS_INDX );
        GLES30.glEnableVertexAttribArray ( VERTEX_COLOR_INDX );

        GLES30.glVertexAttribPointer ( VERTEX_POS_INDX, VERTEX_POS_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                0 );

        GLES30.glVertexAttribPointer ( VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                ( VERTEX_POS_SIZE * 4 ) );

        // Reset to the default VAO
        GLES30.glBindVertexArray ( 0 );

        GLES30.glClearColor ( 1.0f, 1.0f, 1.0f, 0.0f );
    }

    // /
    // Draw a triangle using the shader pair created in onSurfaceCreated()
    //
    public void onDrawFrame ( GL10 glUnused )
    {
        // Set the viewport
        GLES30.glViewport ( 0, 0, mWidth, mHeight );

        // Clear the color buffer
        GLES30.glClear ( GLES30.GL_COLOR_BUFFER_BIT );

        // Use the program object
        GLES30.glUseProgram ( mProgramObject );

        // Bind the VAO
        GLES30.glBindVertexArray ( mVAOId[0] );

        // Draw with the VAO settings
        GLES30.glDrawElements ( GLES30.GL_TRIANGLE_STRIP, mIndicesData.length, GLES30.GL_UNSIGNED_SHORT, 0 );
        Log.d("onDrawFrame: ",   "length of indices : "+mIndicesData.length);
        // Return to the default VAO
        GLES30.glBindVertexArray ( 0 );
    }

    ///
    // Handle surface changes
    //
    public void onSurfaceChanged ( GL10 glUnused, int width, int height )
    {
        mWidth = width;
        mHeight = height;
    }

    // Handle to a program object
    private int mProgramObject;

    // Additional member variables
    private int mWidth;
    private int mHeight;
    private FloatBuffer mVertices;
    private ShortBuffer mIndices;

    // VertexBufferObject Ids
    private int [] mVBOIds = new int[2];

    // VertexArrayObject Id
    private int [] mVAOId = new int[1];
    float s = 1.0f;
    float ns = -1.0f*s;
    float cos_30 = (float)(Math.cos(Math.toRadians(30)));
    float sin_30 = (float)(Math.sin(Math.toRadians(30)));
    // 3 vertices, with (x,y,z) ,(r, g, b, a) per-vertex
    private final float[] mVerticesData =

            {
                0.0f, 0.0f, 0.0f,
                1.0f,0.0f,0.0f,1.0f,
                0.0f, s, 0.0f,
                    1.0f,0.0f,0.0f,1.0f,
                ns*cos_30, s*sin_30, 0.0f,
                    1.0f,0.0f,0.0f,1.0f,
                ns*cos_30, ns*sin_30, 0.0f,
                    1.0f,0.0f,0.0f,1.0f,
                0.0f, ns, 0.0f,
                    1.0f,0.0f,0.0f,1.0f,
                s*cos_30, ns*sin_30, 0.0f,
                    1.0f,0.0f,0.0f,1.0f,
                s*cos_30, s*sin_30, 0.0f,
                    1.0f,0.0f,0.0f,1.0f
            };

//            {
//                    0.0f,  0.5f, 0.0f,        // v0
//                    1.0f,  0.0f, 0.0f, 1.0f,  // c0
//                    -0.5f, -0.5f, 0.0f,        // v1
//                    0.0f,  1.0f, 0.0f, 1.0f,  // c1
//                    0.5f, -0.5f, 0.0f,        // v2
//                    0.0f,  0.0f, 1.0f, 1.0f,  // c2
//            };

    private final short[] mIndicesData =
    {
        0,1,2, 0,2,3, 0,3,4, 0,4,5, 0,5,6, 0,6,1
    };
//            {
//                    0, 1, 2
//            };

    final int VERTEX_POS_SIZE   = 3; // x, y and z
    final int VERTEX_COLOR_SIZE = 4; // r, g, b, and a

    final int VERTEX_POS_INDX   = 0;
    final int VERTEX_COLOR_INDX = 1;

    final int VERTEX_STRIDE     =  ( 4 * ( VERTEX_POS_SIZE +  VERTEX_COLOR_SIZE ) );
}
