package cs371m.tardibear.suito;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import cs371m.tardibear.suito.boids.BatchRenderer;
import cs371m.tardibear.suito.boids.Vec3;
import cs371m.tardibear.suito.boids.Vec4;


public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener{

    private final int CONTEXT_CLIENT_VERSION = 3;
    private GLSurfaceView mGLSurfaceView;
    private BatchRenderer batchRenderer;
    private ObjListAdapter adapter;

    static MediaPlayer track;

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




        batchRenderer = new BatchRenderer(this);
//        Log.d("OBJ_NAME", getIntent().getExtras().getString("OBJ_NAME"));
//        Log.d("OBJ_DEFAULT", Boolean.toString(getIntent().getExtras().getBoolean("OBJ_DEFAULT")));
        batchRenderer.setObj("Triangle", true);

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.surface_view);

        if ( detectOpenGLES30() )
        {
            assert (false);
            // Tell the surface view we want to create an OpenGL ES 3.0-compatible
            // context, and set an OpenGL ES 3.0-compatible renderer.
            mGLSurfaceView.setEGLContextClientVersion ( CONTEXT_CLIENT_VERSION );
            mGLSurfaceView.setRenderer ( batchRenderer );
            Log.d ( "Main", "OpenGL ES 3.2 supported on device.  Exiting..." );

        }
        else
        {
            Log.e ( "SimpleTexture2D", "OpenGL ES 3.0 not supported on device.  Exiting..." );
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
                startActivityForResult(i, RESULT_OK);
                return true;
            }
        });

        track = new MediaPlayer().create(getApplicationContext(), R.raw.pixelparty);
        track.setLooping(true);
        track.start();
    }

    private boolean detectOpenGLES30()
    {
        ActivityManager am =
                ( ActivityManager ) getSystemService ( Context.ACTIVITY_SERVICE );
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        Log.d("info", Integer.toHexString(info.reqGlEsVersion));
        return ( info.reqGlEsVersion >= 0x3200 );
    }

    @Override
    protected void onResume()
    {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
        track.start();

    }

    @Override
    protected void onPause()
    {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
        track.pause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if(resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String name = data.getStringExtra("name");
            float size = data.getFloatExtra("size", 1.0f);
            float r = data.getFloatExtra("red", 0);
            float g = data.getFloatExtra("blue", 0);
            float b = data.getFloatExtra("green", 0);

            float s = data.getFloatExtra("sep", .1f);
            float a = data.getFloatExtra("ali", .1f);
            float c = data.getFloatExtra("coh", .1f);
            Log.d("MainActivity", "creating a new boid");
            batchRenderer.boids.addBoid(name, size, new Vec4(r,g,b,1.0f), new Vec3(s,a,c));

            Log.d("MainActivity", "Flock size : "+batchRenderer.boids.size());
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
        if (id == R.id.action_settings) {
            return true;
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
}
