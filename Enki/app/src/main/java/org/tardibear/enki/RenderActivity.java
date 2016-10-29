package org.tardibear.enki;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kaleb on 11/10/16.
 */
public class RenderActivity extends Activity implements GLSurfaceView.OnTouchListener{

    private GLSurfaceView mSurfaceView;
//    private MainRenderer mRenderer;
    private SimpleRenderer mRenderer;
    private boolean continueMusic;
    private BackgroundMusic bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new GLSurfaceView(this);
        final ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo info = manager.getDeviceConfigurationInfo();
        if(info.reqGlEsVersion >= 0x20000) {
            mSurfaceView.setEGLContextClientVersion(2);

//            mRenderer = new MainRenderer(getApplicationContext());
            mRenderer = new SimpleRenderer(getApplicationContext());

            mSurfaceView.setRenderer(mRenderer);
        }
        else {
            Log.e("RenderActivity", "Device does not support OpenGLES2.0");
        }
        mSurfaceView.setOnTouchListener(this);
        setContentView(mSurfaceView);

        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderer.perturbTriangle();
            }
        });

        continueMusic = true;
        bgm = new BackgroundMusic();
        bgm.start(this, bgm.MUSIC_GAME, true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
        continueMusic = false;
        BackgroundMusic.start(this, bgm.MUSIC_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
        if (!continueMusic) {
            bgm.pause();
        }
    }

    private float  prevX;
    private float  prevY;
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    prevX = event.getX();
                    prevY = event.getY();
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();

                if(x != prevX  || y != prevY )
                    mRenderer.getRenderContext().getCamera().handleSwipe(mRenderer.getRenderContext(), x, y, prevY, prevX, .05f);
                break;
            default:
                break;
        }

        return true;
    }
}
