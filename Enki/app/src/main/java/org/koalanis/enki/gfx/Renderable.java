package org.koalanis.enki.gfx;


import org.koalanis.enki.gfx.Graphics;
import org.koalanis.enki.gfx.Model;
import org.koalanis.enki.gfx.Shader;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Random;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kaleb on 11/10/16.
 *
 * Renderable is a render target which can call draw during the render callback of a opengl program
 * There are some enforcements on the data of which Renderable can consume. The vertex data depends
 * on the functionality of the shader, so we mandate vertex data to contain position, color and uv
 * coordinates for every renderable
 */



public class Renderable {

    // Global Variables

    // Member Variables
    private Model model;
    private final FloatBuffer vertexData;
    private int nVertices;

    public Renderable( float[] data) {
        this.vertexData = ByteBuffer.allocateDirect(data.length * Graphics.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(data).position(0);
        model = new Model();
        nVertices = data.length/Graphics.VERTEX_DATA_SIZE;
    }



    public Model getModel() {return model;}

    public void draw(Shader shader, int texture, RenderContext rc, Model pModel, float[] pColor) {
        shader.use();
        //use pos data
        vertexData.position(Graphics.POSITION_OFFSET);
        GLES20.glVertexAttribPointer(shader.get("a_Position"),
                                     Graphics.POS_DATA_SIZE,
                                     GLES20.GL_FLOAT, false,
                                     Graphics.STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(shader.getPositionHandle());

        //use color data
        vertexData.position(Graphics.COLOR_OFFSET);
        GLES20.glVertexAttribPointer(shader.get("a_Color"),
                Graphics.COL_DATA_SIZE,
                GLES20.GL_FLOAT, false,
                Graphics.STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(shader.getColorHandle());

        // use uv data
        vertexData.position(Graphics.UV_OFFSET);
        GLES20.glVertexAttribPointer(shader.get("a_UV"),
                Graphics.UV_DATA_SIZE,
                GLES20.GL_FLOAT, false,
                Graphics.STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(shader.getUVHandle());


        // pass uniforms in

        // have opengles now refer to texture unit one
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(shader.get("u_Texture"), 0);


        // pass projection matrix here
        GLES20.glUniformMatrix4fv(  shader.get("u_Persp"),
                                    1,
                                    false,
                                    rc.getPerspective(),
                                    0);

//        Log.d("Perspective", Graphics.tensorToString(rc.getPerspective()));

        // pass view matrix here
        GLES20.glUniformMatrix4fv(  shader.get("u_View"),
                1,
                false,
                rc.getCamera().createViewMatrix(),
                0);

//        Log.d("View", Graphics.tensorToString(rc.getCamera().getViewMatrix()));


        //pass model matrix here
        if(pModel == null) {
            GLES20.glUniformMatrix4fv(shader.get("u_Model"), 1, false, model.createModelMatrix(), 0);
//            Log.d("Model", Graphics.tensorToString(model.getModelMatrix()));

        }
        else {
            GLES20.glUniformMatrix4fv(shader.get("u_Model"),1,false, pModel.createModelMatrix(),0);
//            Log.d("Model", Graphics.tensorToString(pModel.getModelMatrix()));
        }


        GLES20.glUniform4fv(shader.get("u_Color"), 1, pColor, 0);

        // render
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, nVertices);

        // disable, idk why
        GLES20.glDisableVertexAttribArray(shader.getPositionHandle());
        GLES20.glDisableVertexAttribArray(shader.getColorHandle());
        GLES20.glDisableVertexAttribArray(shader.getUVHandle());
    }
}
