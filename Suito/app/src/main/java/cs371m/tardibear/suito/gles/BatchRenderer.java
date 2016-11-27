package cs371m.tardibear.suito.gles;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import cs371m.tardibear.suito.R;
import cs371m.tardibear.suito.gfx.Graphics;
import cs371m.tardibear.suito.gfx.Camera;
import cs371m.tardibear.suito.gfx.Model;
import cs371m.tardibear.suito.gfx.Obj;
import cs371m.tardibear.suito.gfx.RenderContext;
import cs371m.tardibear.suito.gfx.Shader;
import cs371m.tardibear.suito.gfx.Sprite;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kaleb on 11/6/2016.
 */

public class BatchRenderer implements GLSurfaceView.Renderer{

    private RenderTarget renderable;
    private RenderContext renderContext;
    private Context parentContext;
    private int mTextureDataHandle;
    private Shader mShader;

    private Obj bunny;


    public BatchRenderer(Context context) {
        parentContext = context;
        renderable = null;
        renderContext = new RenderContext();

        bunny = new Obj(context, R.raw.bunny);
        String TAG = "objLoader";
        for (int i = 0; i < 10; i++) {
            Log.d(TAG,Float.toString(bunny.vertices[i]));
            Log.d(TAG,Short.toString(bunny.indices[i]));

        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        int[] temp = new int[1];
        GLES30.glGetIntegerv(GLES30.GL_MAX_UNIFORM_BLOCK_SIZE, IntBuffer.wrap(temp));
        int tempp = temp[0];
        Log.d("onSurfaceCreate", "MAX_UNIFORM_BLOCK_SIZE = "+tempp);


//        mShader = new Shader().attach(GLES30.GL_VERTEX_SHADER, Graphics.vShaderStr).attach(GLES30.GL_FRAGMENT_SHADER, Graphics.fShaderStr);

        mShader = new Shader().attach(GLES30.GL_VERTEX_SHADER, parentContext, R.raw.vert_simple).attach(GLES30.GL_FRAGMENT_SHADER, parentContext, R.raw.frag_simple);

        String[] atts = {"a_position", "a_color", "a_uv"};
        String[] unis = {"u_model", "u_view", "u_persp", "u_color"};
        mShader.compile(atts, unis);

        GLES30.glClearColor(1.0f,1.f,1.0f,1.0f);

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;


        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        renderContext.setCamera(new Camera());
        renderContext.getCamera().setEye(eyeX, eyeY, eyeZ);
        renderContext.getCamera().setLook(lookX, lookY, lookZ);
        renderContext.getCamera().setUp(upX, upY, upZ);
        renderContext.getCamera().createViewMatrix();
        renderContext.update();
        // texture id
        mTextureDataHandle = Graphics.loadTexture(parentContext, R.drawable.checkboard_map);


        //TODO: use Obj
        renderable = new RenderTarget(bunny);
//        renderable = new RenderTarget(Graphics.getUnitHexagonVertices(), Graphics.getUnitHexagonIndicies());

//        Sprite sprite = new Sprite();

        renderable.setColor(new float[]{1.0f, 0.0f, 0.0f, 1.0f});
        renderable.getModel().setScale(10.0f);
        renderable.getModel().createModelMatrix();
//        sprite.setColor(new float[] {0.0f, 1.0f, 0.0f, 1.0f});
        renderable.setTexture(mTextureDataHandle);
        renderable.attachShader(mShader);
        renderable.init();
//
//        renderables.add(rt);
//        renderables.add(sprite);


//        for (Renderable r:
//                renderables) {
//            r.attachShader(mShader);
//            r.init();
//            Log.d("RenderLoop", Graphics.tensorToString(renderContext.getPerspective()));
//            Log.d("RenderLoop", Graphics.tensorToString(renderContext.getCamera().getViewMatrix()));
//        }

//        Model t = new Model();
//        float[] fff = new float[4*20*grid.getC()*grid.getR()];
//        FloatBuffer fb = FloatBuffer.wrap(fff);
////        FloatBuffer fb = ByteBuffer.allocateDirect(4*20*grid.getC()*grid.getR()).order(ByteOrder.nativeOrder()).asFloatBuffer();
//        for (HexTile hexTile:
//                grid.map.values()) {
//            float[] pos = grid.hexToPixel(hexTile);
//            t.setTranslate(pos[0],pos[1],0.0f);
//            t.setScale(.25f);
//            fb.put(t.createModelMatrix());
//            fb.put(Graphics.getRandomColor4());
//        }


//        float[] translations = new float[2*(grid.getR()* grid.getC())];
//        for (int i = 0; i < grid.getR(); i++) {
//            for (int j = 0; j < grid.getC(); j++) {
//                float[] pos = grid.hexToPixel(grid.get(i,j));
//                translations[2*(i*grid.getC()+j)] = pos[0];
//                translations[2*(i*grid.getC()+j)+1] = pos[1];
//            }
//        }
//
//        it = new InstancedTarget(Graphics.getUnit2DHexagonVertices(), Graphics.getUnitHexagonIndicies(), fff);
//        it.setTexture(mTextureDataHandle);
//        it.attachShader(mInstancedShader);
//        it.init();
//        renderables.add(it);
//        it.setColor(new float[] {0.0f, 0.0f, 1.0f, 1.0f});
//        it.getModel().scale(.5f);
//        it.getModel().createModelMatrix();
//        renderables.add(it);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width, height);
        renderContext.setWidth(width);
        renderContext.setHeight(height);
        renderContext.update();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Set the viewport
        GLES30.glViewport ( 0, 0, renderContext.getWidth(), renderContext.getHeight() );

        // Clear the color buffer
        GLES30.glClear ( GLES30.GL_COLOR_BUFFER_BIT );
        renderable.draw(renderContext);

//        for (Renderable r:
//                renderables) {
//            r.draw(renderContext);
//        }

    }

    public void onPause(){}
    public void onResume(){}

}
