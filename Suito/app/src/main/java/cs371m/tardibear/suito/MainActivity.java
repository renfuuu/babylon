package cs371m.tardibear.suito;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import cs371m.tardibear.suito.boids.BatchRenderer;
import cs371m.tardibear.suito.boids.Vec3;
import cs371m.tardibear.suito.boids.Vec4;
import cs371m.tardibear.suito.gfx.Graphics;
import cs371m.tardibear.suito.gfx.RenderContext;


public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener, SensorEventListener, GLSurfaceView.OnTouchListener{

    private static final float EPSILON = 0.001f;
    private static final int CONTEXT_CLIENT_VERSION = 3;
    private GLSurfaceView mGLSurfaceView;
    private BatchRenderer batchRenderer;
    private ObjListAdapter adapter;



    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static final float nanoToSeconds  = 1.0f/1000000000.0f;
    private final float[] deviceRotationVector = new float[4];
    private final float[] baseOrientationVector = new float[4];
    private final float[] baseOrientationMatrix = new float[9]; //3x3
    private final float[] currentOrientationMatrix = new float[9]; //3x3
    private Vec3 delta = new Vec3();
    private float sensorTimestamp;

    private boolean hasReferenceVector;

    static MediaPlayer track;
    static boolean playing = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        hasReferenceVector = false;

        ImageView imageCoachingOverlay = (ImageView) findViewById(R.id.imageCoachingOverlay);
        imageCoachingOverlay.setVisibility(View.VISIBLE);
        imageCoachingOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setVisibility(View.GONE);

                return false;
            }
        });


        batchRenderer = new BatchRenderer(this);
        batchRenderer.setObj("Triangle", true);

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.surface_view);

        if ( detectOpenGLES30() )
        {
            // Tell the surface view we want to create an OpenGL ES 3.0-compatible
            // context, and set an OpenGL ES 3.0-compatible renderer.
            mGLSurfaceView.setEGLContextClientVersion ( CONTEXT_CLIENT_VERSION );
            mGLSurfaceView.setRenderer ( batchRenderer );
            Log.d ( "Main", "OpenGL ES 3.2 supported on device." );

        }
        else
        {
            Log.e ( "Super Boids Sim", "OpenGL ES 3.0 not supported on device.  Exiting..." );
            finish();
        }

        adapter = new ObjListAdapter(this, batchRenderer.boids.getList());

        ListView listView = (ListView) findViewById(R.id.drawer_list_view);
        listView.setAdapter(adapter);
        int width = getResources().getDisplayMetrics().widthPixels/3*2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) listView.getLayoutParams();
        params.width = width;
        listView.setLayoutParams(params);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (batchRenderer.boids.getList()) {
                    batchRenderer.boids.addRandomBoid();
                    adapter.setObjList(batchRenderer.boids.getList());
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent i = new Intent(getApplicationContext(), BoidCreator.class);
                startActivityForResult(i, 1);
                return true;
            }
        });

        track = new MediaPlayer().create(getApplicationContext(), R.raw.pixelparty);
        track.setLooping(true);
        track.start();
        playing = true;


        initHardwareSensors();


        mGLSurfaceView.setOnTouchListener(this);

    }

    private boolean detectOpenGLES30()
    {
        ActivityManager am =
                ( ActivityManager ) getSystemService ( Context.ACTIVITY_SERVICE );
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        Log.d("info", Integer.toHexString(info.reqGlEsVersion));
        return ( info.reqGlEsVersion >= 0x3200 );
    }

//    following code is set up gyroscope is inspired by example on Android API
//    https://developer.android.com/guide/topics/sensors/sensors_motion.html

    private void initHardwareSensors() {
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }


    private float sin(float s) {
        return ((float)Math.sin(s));
    }

    private float cos(float s) {
        return ((float)Math.cos(s));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {



        if(sensorTimestamp != 0) {
            final float dT = (event.timestamp - sensorTimestamp) * nanoToSeconds;
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];
            double dd = (double) (axisX * axisX + axisY * axisY + axisZ * axisZ);
            float omegaMagnitude = (float) (Math.sqrt(dd));
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = sin(thetaOverTwo);
            float cosThetaOverTwo = cos(thetaOverTwo);
            deviceRotationVector[0] = sinThetaOverTwo * axisX;
            deviceRotationVector[1] = sinThetaOverTwo * axisY;
            deviceRotationVector[2] = sinThetaOverTwo * axisZ;
            deviceRotationVector[3] = cosThetaOverTwo;
        }
        sensorTimestamp = event.timestamp;

        if(!hasReferenceVector) {
            System.arraycopy(baseOrientationVector, 0, deviceRotationVector, 0, deviceRotationVector.length);
        }

        float[] rotMatrix = new float[9];
        float[] baseMatrix = new float[9];

        if(!hasReferenceVector) {
            SensorManager.getRotationMatrixFromVector(baseMatrix, deviceRotationVector);
            hasReferenceVector = true;
        }
        SensorManager.getRotationMatrixFromVector(rotMatrix, deviceRotationVector);

        Vec3 z  = new Vec3(rotMatrix[6], rotMatrix[7], rotMatrix[8]);

        Vec3 d = Vec3.unitZ().sub(z);

//        Log.d("Sensor X", Graphics.tensorToString(x.asArray()));
//        Log.d("Sensor Y", Graphics.tensorToString(y.asArray()));
        Log.d("Sensor Z", Graphics.tensorToString(d.asArray()));
//        Log.d("Sensor", Graphics.tensorToString((a.sub(b)).asArray()));
//        Log.d("Sensor", Graphics.tensorToString(rotMatrix));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    @Override
    protected void onResume()
    {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
        if(playing){
            track.start();
        }
        playing = true;

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause()
    {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
        if(!playing) {
            track.pause();
            playing = false;
        }

        mSensorManager.unregisterListener(this);
        hasReferenceVector = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if(resultCode == RESULT_OK) {

            String name = data.getStringExtra("name");
            float size = data.getFloatExtra("size", 1.0f);
            Log.d("size", Float.toString(size));
            float r = data.getFloatExtra("red", 0);
            float g = data.getFloatExtra("green", 0);
            float b = data.getFloatExtra("blue", 0);

            float s = data.getFloatExtra("sep", .1f);
            float a = data.getFloatExtra("ali", .1f);
            float c = data.getFloatExtra("coh", .1f);
            Log.d("MainActivity", "creating a new boid");
            synchronized (batchRenderer.boids.getList()) {
                batchRenderer.boids.addBoid(name, size, new Vec4(r, g, b, 1.0f), new Vec3(s, a, c));
            }
            adapter.setObjList(batchRenderer.boids.getList());

            Log.d("MainActivity", "Flock size : " + batchRenderer.boids.size());
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_play_pause) {
            if(track.isPlaying()){
                track.pause();
                item.setIcon(R.drawable.ic_play_arrow_white_48dp);
                playing = false;
            }
            else{
                track.start();
                item.setIcon(R.drawable.ic_pause_white_48dp);
                playing = true;
            }
            return true;
        }
        else if(id == R.id.zoom_in) {
            batchRenderer.getRenderContext().getCamera().zoomIn();
            batchRenderer.getRenderContext().getCamera().createViewMatrix();
        }
        else if(id == R.id.zoom_out){
            batchRenderer.getRenderContext().getCamera().zoomOut();
            batchRenderer.getRenderContext().getCamera().createViewMatrix();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_background) {
            // Handle the camera action
        } else if (id == R.id.nav_color) {

        } else if (id == R.id.nav_texture) {

        } else if (id == R.id.nav_collection) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("x : ").append(motionEvent.getX()).append("y : ").append(motionEvent.getY());
        Log.d("onTouch", sb.toString());

        RenderContext rc = batchRenderer.getRenderContext();
        float screenHeight = rc.getHeight();
        float screenWidth = rc.getWidth();

        float[] m = new float[16];
        float[] inv = new float[16];
        Matrix.multiplyMM(m,0, rc.getPerspective(), 0, rc.getCamera().getViewMatrix(),0);
        Matrix.invertM(inv, 0, m, 0);
        int realY = (int)(screenHeight - motionEvent.getY());
        float[] ndc = new float[4];
        float[] w_coor = new float[4];
        ndc[0] = (float)(motionEvent.getX()*2.0/screenWidth-1.0f);
        ndc[1] = (float)(motionEvent.getY()*2.0/screenWidth-1.0f);
        ndc[2] = -1.0f;
        ndc[3] = 1.0f;

        Matrix.multiplyMV(w_coor, 0, inv, 0, ndc, 0);
        if(w_coor[3] == 0) {
            Log.e("onTouch", "divide by zero, wtf");
            return false;
        }
        w_coor[0] /= w_coor[3];
        w_coor[1] /= w_coor[3];
        Log.d("onTouch", Graphics.tensorToString(w_coor));
        batchRenderer.boids.gravitate(100.0f, new Vec3(w_coor[0], w_coor[1], 0.0f));
        return true;
    }
}
