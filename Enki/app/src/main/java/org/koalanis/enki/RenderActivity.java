package org.koalanis.enki;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.koalanis.enki.MainRenderer;

/**
 * Created by kaleb on 11/10/16.
 */
public class RenderActivity extends Activity{

    private GLSurfaceView mSurfaceView;
    private MainRenderer mRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new GLSurfaceView(this);
        final ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo info = manager.getDeviceConfigurationInfo();
        if(info.reqGlEsVersion >= 0x20000) {
            mSurfaceView.setEGLContextClientVersion(2);
            mRenderer = new MainRenderer(getApplicationContext());
            mSurfaceView.setRenderer(mRenderer);
        }
        else {
            Log.e("RenderActivity", "Device does not support OpenGLES2.0");
        }
        setContentView(mSurfaceView);

        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderer.perturbTriangle();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
    }

}
