package org.tardibear.enki.gfx3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by kaleb on 11/10/16.
 */
public class        Graphics {

    public static final int BYTES_PER_FLOAT = 4;
    public static final int POSITION_OFFSET = 0;
    public static final int POS_DATA_SIZE = 3;
    public static final int COLOR_OFFSET = 3;
    public static final int COL_DATA_SIZE = 4;
    public static final int UV_OFFSET = 7;
    public static final int UV_DATA_SIZE = 2;
    public static final int VERTEX_DATA_SIZE = POS_DATA_SIZE + COL_DATA_SIZE + UV_DATA_SIZE;
    public static final int STRIDE = VERTEX_DATA_SIZE * BYTES_PER_FLOAT;

    public static final float[] getUnitHexagon() {
        float s = 1.0f;
        float ns = -1.0f * s;
        float cos_30 = (float) (Math.cos(Math.toRadians(30)));
        float sin_30 = (float) (Math.sin(Math.toRadians(30)));
        return new float[]{
                // fan of hexagon counter clockwise

                0.0f, 0.0f, 0.0f,
                0.0f, s, 0.0f,
                ns * cos_30, s * sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                ns * cos_30, s * sin_30, 0.0f,
                ns * cos_30, ns * sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                ns * cos_30, ns * sin_30, 0.0f,
                0.0f, ns, 0.0f,

                0.0f, 0.0f, 0.0f,
                0.0f, ns, 0.0f,
                s * cos_30, ns * sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                s * cos_30, ns * sin_30, 0.0f,
                s * cos_30, s * sin_30, 0.0f,

                0.0f, 0.0f, 0.0f,
                s * cos_30, s * sin_30, 0.0f,
                0.f, s, 0.0f,};

    }

    public static float[] getUnitHexagonVertices() {
        float s = 1.0f;
        float ns = -1.0f * s;
        float cos_30 = (float) (Math.cos(Math.toRadians(30)));
        float sin_30 = (float) (Math.sin(Math.toRadians(30)));
        return new float[]{
                // fan of hexagon counter clockwise

                0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                0.0f, s, 0.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                ns * cos_30, s * sin_30, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                ns * cos_30, ns * sin_30, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                0.0f, ns, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                s * cos_30, ns * sin_30, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                s * cos_30, s * sin_30, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f};

    }

    public static float[] getUnit2DHexagonVertices() {
        float s = 1.0f;
        float ns = -1.0f * s;
        float cos_30 = (float) (Math.cos(Math.toRadians(30)));
        float sin_30 = (float) (Math.sin(Math.toRadians(30)));
        return new float[]{
                // fan of hexagon counter clockwise

                0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                0.0f, s,
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                ns * cos_30, s * sin_30,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                ns * cos_30, ns * sin_30,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                0.0f, ns,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                s * cos_30, ns * sin_30,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                s * cos_30, s * sin_30,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f};

    }

    public static short[] getUnitHexagonIndicies() {
        return new short[]{
                0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 6, 0, 6, 1
        };
    }

    public static final float[] getUnitTexturedColoredSquare() {
        return new float[]{
                0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f,

                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f,

                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f,

                1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f,

                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f

        };
    }

    public static float[] getUnitTexturedSquareVertices() {
        return new float[]{
                0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f,

                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f,

                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f,

                1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f
        };
    }

    public static short[] getUnitTexturedSquareIndices() {
        return new short[]{
                0, 1, 2,
                0, 2, 3
        };
    }

    public static final String vertexShader = ""
            + "uniform mat4 u_Model;"
            + "uniform vec4 u_Color;"
            + "uniform mat4 u_View;"
            + "uniform mat4 u_Persp;"
            + "attribute vec3 a_Position;"
            + "attribute vec4 a_Color;"
            + "attribute vec2 a_UV;"
            + "varying vec4 v_Color;"
            + "varying vec2 v_UV;"
            + "void main() {"
            + " v_Color = a_Color*u_Color;"
            + " v_UV = a_UV;"
            + " mat4 MVP = u_Persp*u_View*u_Model;"
            + " gl_Position = MVP * vec4(a_Position, 1.0f);"
            + "}";


    public static final String fragmentShader = "precision mediump float;"
            + "varying vec4 v_Color;"
            + "uniform sampler2D u_Texture;"
            + "varying vec2 v_UV;"
            + "void main() {"
            + " gl_FragColor = v_Color * texture2D(u_Texture, v_UV);"
            + "}";


    public static int loadShaderFromRaw(Context context, int type, int id) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(id)));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Log.d("shaderSource", sb.toString());
            return loadShader(type, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static int loadShader(int type, String shaderCode) throws Exception{

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        final int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
        String output = GLES30.glGetShaderInfoLog(shader);
        if (compileStatus[0] == GLES30.GL_FALSE) {
            GLES30.glDeleteShader(shader);
            shader = 0;
        }

        String strType = "";
        switch (type) {
            case GLES30.GL_VERTEX_SHADER:
                strType += "Vertex";
                break;

            case GLES30.GL_FRAGMENT_SHADER:
                strType += "Fragment";
                break;

            default:
                break;
        }

        Log.d("loadShader", output);
        if (shader == 0) {
            throw new RuntimeException("Error Creating " + strType + "shader");
        }

        return shader;
    }

    public static float[] getRandomColor4() {
        float r = (float)Math.random();
        float g = (float)Math.random();
        float b = (float)Math.random();
        return new float[] {r,g,b,1.0f};

    }

    public static int loadTexture(final Context context, final int resourceID) {
        final int[] textureHandle = new int[1];
        GLES30.glGenTextures(1, textureHandle, 0);
        if (textureHandle[0] != 0) {
            final BitmapFactory.Options option = new BitmapFactory.Options();
            option.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }

        return textureHandle[0];
    }

    public static String tensorToString(float[] mat) {
        String output = "";
        for (int i = 0; i < mat.length; i++) {
            if (i % 4 == 0) {
                output += "|";
            }
            output += String.format("%.3f", mat[i]) + "|";
            if (i % 4 == 3) {
                output += "\n";
            }
        }
        return output;
    }


    public static final String vShaderStr =
            "#version 300 es                            \n" +
                    "layout(location = 0) in vec3 a_position;   \n" +
                    "layout(location = 1) in vec4 a_color;      \n" +
                    "layout(location = 2) in vec2 a_uv;         \n" +
                    "uniform mat4 u_model;                      \n" +
                    "uniform mat4 u_view;                       \n" +
                    "uniform mat4 u_persp;                      \n" +
                    "uniform vec4 u_color;                      \n" +
                    "out vec2 v_uv;                             \n" +
                    "out vec4 v_color;                          \n" +
                    "void main()                                \n" +
                    "{                                          \n" +
                    "    mat4 MVP = u_persp*u_view*u_model;     \n" +
                    "    v_color = a_color*u_color;             \n" +
                    "    v_uv = a_uv;                           \n" +
                    "    gl_Position = MVP*vec4(a_position, 1.0f);          \n" +
                    "}";

    public static final String fShaderStr =
            "#version 300 es            \n" +
                    "precision mediump float;   \n" +
                    "in vec4 v_color;           \n" +
                    "uniform sampler2D u_texture;\n" +
                    "in vec2 v_uv ;              \n" +
                    "layout(location = 0) out vec4 o_fragColor;             \n" +
                    "void main()                \n" +
                    "{                          \n" +
                    "    o_fragColor = v_color * texture(u_texture, v_uv); \n" +
                    "}";


    public static final String vMinShaderStr =
            "#version 300 es                            \n" +
                    "layout(location = 0) in vec4 a_position;   \n" +
                    "layout(location = 1) in vec4 a_color;      \n" +
                    "out vec4 v_color;                          \n" +
                    "void main()                                \n" +
                    "{                                          \n" +
                    "    v_color = a_color;                     \n" +
                    "    gl_Position = a_position;              \n" +
                    "}";


    public static final String fMinShaderStr =
            "#version 300 es            \n" +
                    "precision mediump float;   \n" +
                    "in vec4 v_color;           \n" +
                    "out vec4 o_fragColor;      \n" +
                    "void main()                \n" +
                    "{                          \n" +
                    "    o_fragColor = v_color; \n" +
                    "}";



    public static final String vInstanceShaderStr =
            "#version 300 es                            \n" +
                    "layout(location = 0) in vec2 a_position;   \n" +
                    "layout(location = 1) in vec4 a_color;      \n" +
                    "layout(location = 2) in vec2 a_uv;         \n" +
                    "layout(location = 3) in vec2 a_offset;     \n" +
                    "uniform mat4 u_model;                      \n" +
                    "uniform mat4 u_view;                       \n" +
                    "uniform mat4 u_persp;                      \n" +
                    "uniform vec4 u_color;                      \n" +
                    "out vec2 v_uv;                             \n" +
                    "out vec4 v_color;                          \n" +
                    "void main()                                \n" +
                    "{                                          \n" +
                    "    mat4 MVP = u_persp*u_view*u_model;     \n" +
                    "    v_color = a_color*u_color;             \n" +
                    "    v_uv = a_uv;                           \n" +
                    "    gl_Position = MVP*vec4(a_position+a_offset, 0.0f, 1.0f);          \n" +
                    "}";

    public static final String fInstanceShaderStr =
            "#version 300 es            \n" +
                    "precision mediump float;   \n" +
                    "in vec4 v_color;           \n" +
                    "uniform sampler2D u_texture;\n" +
                    "in vec2 v_uv ;              \n" +
                    "layout(location = 0) out vec4 o_fragColor;             \n" +
                    "void main()                \n" +
                    "{                          \n" +
                    "    o_fragColor = v_color * texture(u_texture, v_uv); \n" +
                    "}";



}

