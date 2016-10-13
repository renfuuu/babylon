package org.koalanis.enki;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.koalanis.enki.GLRenderer;

public class MainActivity extends AppCompatActivity{

    private GLView mGLView;


    private final int OPENGL_VERSION = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new GLView(this);

        if( handleOpenGLInit() ) {
            Log.d("MainActivity", "OpenGLES 3.0 supported");
        }
        else {
            Log.e("MainActivity", "OpenGLES 3.0 not supported");
//            mGLView.setEGLContextClientVersion(OPENGL_VERSION - 1);
//            mGLView.setRenderer( new GLRenderer( this ));
            finish();
        }

        setContentView(R.layout.activity_main);
    }

    private boolean handleOpenGLInit() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        Log.d("OPENGL VERSION", ""+info.reqGlEsVersion);
        return (info.reqGlEsVersion >= 0x20000);
    }
}