package org.tardibear.enki;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.tardibear.enki.mkii.BatchRenderer;

/**
 * Created by kaleb on 11/10/16.
 */
public class RenderActivity extends Activity implements GLSurfaceView.OnTouchListener {

    private GLSurfaceView mSurfaceView;
//    private SimpleRenderer mRenderer;

    private BatchRenderer mRenderer;

    private boolean continueMusic;
    private BackgroundMusic bgm;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new GLSurfaceView(this);
        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo info = manager.getDeviceConfigurationInfo();
        if (info.reqGlEsVersion >= 0x20000) {
            mSurfaceView.setEGLContextClientVersion(2);

//            mRenderer = new SimpleRenderer(getApplicationContext());
            mRenderer = new BatchRenderer(getApplicationContext());
            mSurfaceView.setRenderer(mRenderer);
        } else {
            Log.e("RenderActivity", "Device does not support OpenGLES2.0");
        }
        mSurfaceView.setOnTouchListener(this);
        setContentView(mSurfaceView);

//        mSurfaceView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRenderer.perturbTriangle();
//            }
//        });

        //background music
        continueMusic = true;
        bgm = new BackgroundMusic();
        bgm.start(this, bgm.MUSIC_GAME, true);

        //Loot
        TreasureChest chest = new TreasureChest();
        final int lootTotals[] = new int[chest.getLootSize()];

        //Button
        RelativeLayout rl = new RelativeLayout(this);
        ImageButton btn = new ImageButton(this);
        btn.setImageResource(R.drawable.ic_inbox_black_24dp);
        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);


        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 1);

        btn.setLayoutParams(rlp);
        btn.getLayoutParams().height = 150;
        btn.getLayoutParams().width = 200;
        btn.requestLayout();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inventory = new Intent(getApplicationContext(), InventoryActivity.class);
                Bundle lootBundle = new Bundle();
                lootBundle.putIntArray("loot", lootTotals);
                inventory.putExtras(lootBundle);
                startActivity(inventory);
            }
        });


        rl.addView(btn);
        rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.addContentView(rl, rlp);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
//        continueMusic = false;
//        BackgroundMusic.start(this, bgm.MUSIC_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
//        if (!continueMusic) {
//            bgm.pause();
//        }
    }

    private float prevX;
    private float prevY;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevY = event.getY();
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();

//                if (x != prevX || y != prevY)
//                    mRenderer.getRenderContext().getCamera().handleSwipe(mRenderer.getRenderContext(), x, y, prevY, prevX, .05f);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Render Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
