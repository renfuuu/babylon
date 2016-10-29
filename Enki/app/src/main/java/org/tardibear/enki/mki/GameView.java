package org.tardibear.enki.mki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by kaleb on 25/10/16.
 */

public class GameView extends SurfaceView implements Runnable{
    Paint paint;
    int screenHeight;
    int screenWidth;
    float density;
    volatile int cx;
    volatile int cy;
    int radius;
    volatile int opx = 1;
    volatile int opy = 1;

    long fps;
    long frametime;
    long startFrameTime;



    volatile boolean running  = false;
    SurfaceHolder holder;
    Thread renderThread = null;
    private boolean isMoving;
    private int bitXPosition;
    private Bitmap bitmap;
    private int moveSpeedPerSecond = 20;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenHeight = dm.widthPixels;
        density = dm.density;
        radius = Math.round(20 * density);
        isMoving = false;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.sym_def_app_icon);


    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
        opx = 1; opy = 1;
        cx = 50; cy = 50;
    }

    @Override
    public void run() {
        while(running) {

            startFrameTime = System.currentTimeMillis();

            update();


            draw();

            frametime = System.currentTimeMillis() - startFrameTime;
            if(frametime > 0) {
                fps  = 1000/frametime;
            }
        }
    }

    private void update() {
       if(isMoving) {
           bitXPosition = (int) (bitXPosition + (moveSpeedPerSecond/fps));
       }

    }

    public void draw() {
        if(holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255,2,26,255));
            paint.setColor(Color.GREEN);
            paint.setTextSize(45);
            canvas.drawText("FPS: "+fps, 20, 40, paint);
            canvas.drawBitmap(bitmap, bitXPosition, 200, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }



    public void pause() {
        running = false;
        boolean retry = true;
        while(retry) {
            try {
                renderThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e("GameView", "pause", e);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
            default:
        }
        return true;
    }
}
