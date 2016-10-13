package org.koalanis.enki.gfx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by kaleb on 11/10/16.
 */
public class Graphics {

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

//        final int[] compileStatus = new int [1];
//        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
//        if(compileStatus[0] == 0) {
//            GLES20.glDeleteShader(shader);
//            shader = 0;
//        }

//        String strType = "";
//        switch (type) {
//            case GLES20.GL_VERTEX_SHADER:
//                strType += "Vertex";
//                break;
//
//
//            case GLES20.GL_FRAGMENT_SHADER:
//                strType += "Fragment";
//                break;
//
//            default:
//                break;
//        }
//
//        if(shader == 0)
//        {
//            throw new RuntimeException("Error Creating "+ strType + "shader");
//        }

        return shader;
    }

    public static int loadTexture(final Context context, final int resourceID) {
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        if(textureHandle[0] != 0)
        {
            final BitmapFactory.Options option = new BitmapFactory.Options();
            option.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }

        return textureHandle[0];
    }
}

