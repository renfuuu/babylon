package cs371m.tardibear.suito.boids;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import junit.framework.Assert;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cs371m.tardibear.suito.R;
import cs371m.tardibear.suito.gfx.Camera;
import cs371m.tardibear.suito.gfx.Graphics;
import cs371m.tardibear.suito.gfx.Obj;
import cs371m.tardibear.suito.gfx.RenderContext;
import cs371m.tardibear.suito.gfx.Shader;

/**
 * Created by kaleb on 11/6/2016.
 */

public class BatchRenderer implements GLSurfaceView.Renderer{

    private RenderTarget renderable;
    private RenderContext renderContext;
    private Context parentContext;
    private int mTextureDataHandle;
    private Shader mShader;

    private Obj mesh;
    private int defaultMeshInteger;
    private final int BUNNY_NUMBER = 1;
    private final int DRAGON_NUMBER = 2;
    private final int TRIANGLE_NUMBER = 3;
    private final int SPHERE_NUMBER = 4;
    private final int CUSTOM_NUMBER = 5;
    private int currentMesh;
    private String objFile;

    public Flock boids;

    private float worldSize;



    public BatchRenderer(Context context) {
        parentContext = context;
        renderable = null;
        renderContext = new RenderContext();
        boids = new Flock(3, 4, 0);
    }

//    public BatchRenderer(Context context, String objFile, boolean defaultMesh){
//        this(context);
//        setObj(objFile, defaultMesh);
//    }

    public void setObj(String objFile, boolean defaultMesh){
        //objFile can be the content of the file or the name of the default objs
        if(defaultMesh){
            switch(objFile){
                case "Bunny":
                    currentMesh = BUNNY_NUMBER;
                    break;
                case "Dragon":
                    currentMesh = DRAGON_NUMBER;
                    break;
                case "Triangle":
                    currentMesh = TRIANGLE_NUMBER;
                    break;
                case "Sphere":
                    currentMesh = SPHERE_NUMBER;
                default:
                    break;
            }
        }
        else{
            currentMesh = CUSTOM_NUMBER;
            this.objFile = objFile;
        }


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        int[] temp = new int[1];
        GLES30.glGetIntegerv(GLES30.GL_MAX_UNIFORM_BLOCK_SIZE, IntBuffer.wrap(temp));
        int tempp = temp[0];
//        Log.d("onSurfaceCreate", "MAX_UNIFORM_BLOCK_SIZE = "+tempp);


//        mShader = new Shader().attach(GLES30.GL_VERTEX_SHADER, Graphics.vShaderStr).attach(GLES30.GL_FRAGMENT_SHADER, Graphics.fShaderStr);

        mShader = new Shader().attach(GLES30.GL_VERTEX_SHADER, parentContext, R.raw.vert_simple).attach(GLES30.GL_FRAGMENT_SHADER, parentContext, R.raw.frag_simple);

        String[] atts = {"a_position", "a_color", "a_uv"};
        String[] unis = {"u_model", "u_view", "u_persp", "u_color"};
        mShader.compile(atts, unis);

        GLES30.glClearColor(1.0f,1.f,1.0f,1.0f);

        switch(currentMesh){
            case BUNNY_NUMBER:
                mesh = new Obj(parentContext, R.raw.bunny);
                break;
            case DRAGON_NUMBER:
                mesh = new Obj(parentContext, R.raw.dragon);
                break;
            case TRIANGLE_NUMBER:
                mesh = new Obj(parentContext, R.raw.triangle);
                break;
            case SPHERE_NUMBER:
                mesh = new Obj(parentContext, R.raw.sphere);
                break;
            case CUSTOM_NUMBER:
                mesh = new Obj(objFile);
            default:
                break;
        }

        Assert.assertNotNull(mesh);

        worldSize = 10.0f;

        String TAG = "objLoader";
        for (int i = 0; i < 3; i++) {
            Log.d(TAG,Float.toString(mesh.vertices[i]));
            Log.d(TAG,Short.toString(mesh.indices[i]));

        }

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 5.0f;

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


        renderable = new RenderTarget(mesh);
//        renderable = new RenderTarget(Graphics.getUnitHexagonVertices(), Graphics.getUnitHexagonIndicies());

//        Sprite sprite = new Sprite();

        renderable.setColor(new float[]{1.0f, 0.0f, 0.0f, 1.0f});
        renderable.getModel().createModelMatrix();
//        sprite.setColor(new float[] {0.0f, 1.0f, 0.0f, 1.0f});
        renderable.setTexture(mTextureDataHandle);
        renderable.attachShader(mShader);
        renderable.init();

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
        synchronized (boids.getList()) {
            for (Boid b :
                    boids.flock) {
                renderable.getModel().setTranslate(b.getLocation().asArray());
                renderable.getModel().setScale(b.getSize()+worldSize);
                renderable.setColor(b.getColor());
                renderable.getModel().createModelMatrix();
                renderable.draw(renderContext);
//                Log.d("renderloop", b.toString());
            }
            boids.update();
        }
    }

    public void onPause(){}
    public void onResume(){}

    public RenderContext getRenderContext() {
        return renderContext;
    }
}
