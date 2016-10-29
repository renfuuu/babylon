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

    public static final int BYTES_PER_FLOAT = 4;
    public static final int POSITION_OFFSET = 0;
    public static final int POS_DATA_SIZE  = 3;
    public static final int COLOR_OFFSET = 3;
    public static final int COL_DATA_SIZE = 4;
    public static final int UV_OFFSET = 7;
    public static final int UV_DATA_SIZE = 2;
    public static final int VERTEX_DATA_SIZE = POS_DATA_SIZE+COL_DATA_SIZE+UV_DATA_SIZE;
    public static final int STRIDE = VERTEX_DATA_SIZE * BYTES_PER_FLOAT;

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
    public static final float[] getUnitTexturedColoredSquare() {
      return new float[] {
              0.0f, 0.0f, 0.0f,
              1.0f,1.0f,1.0f,1.0f,
              0.0f, 1.0f,
              1.0f, 1.0f, 0.0f,
              1.0f,1.0f,1.0f,1.0f,
              1.0f, 0.0f,
              0.0f, 1.0f, 0.0f,
              1.0f,1.0f,1.0f,1.0f,
              0.0f, 0.0f,

              0.0f, 0.0f, 0.0f,
              1.0f,1.0f,1.0f,1.0f,
              0.0f, 1.0f,
              1.0f, 0.0f, 0.0f,
              1.0f,1.0f,1.0f,1.0f,
              1.0f,1.0f,
              1.0f, 1.0f, 0.0f,
              1.0f,1.0f,1.0f,1.0f,
              1.0f,0.0f

      }  ;
    }

    public static final String vertexShader = "uniform mat4 u_Model;"
            +"uniform vec4 u_Color;"
            +"uniform mat4 u_View;"
            +"uniform mat4 u_Persp;"
            +"attribute vec3 a_Position;"
            +"attribute vec4 a_Color;"
            +"attribute vec2 a_UV;"
            +"varying vec4 v_Color;"
            +"varying vec2 v_UV;"
            +"void main() {"
            +" v_Color = a_Color*u_Color;"
            +" v_UV = a_UV;"
            +" mat4 MVP = u_Persp*u_View*u_Model;"
            +" gl_Position = MVP * vec4(a_Position, 1.0f);"
            +"}";


    public static final String fragmentShader ="precision mediump float;"
            +"varying vec4 v_Color;"
            +"uniform sampler2D u_Texture;"
            +"varying vec2 v_UV;"
            +"void main() {"
            +" gl_FragColor = v_Color * texture2D(u_Texture, v_UV);"
            +"}";

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

    public static String tensorToString(float[] mat) {
        String output = "";
        for(int i = 0; i < mat.length; i++) {
            if(i%4 == 0) {
                output += "|";
            }
            output += String.format("%.3f", mat[i]) + "|";
            if(i%4 == 3) {
                output += "\n";
            }
        }
        return output;
    }

}

