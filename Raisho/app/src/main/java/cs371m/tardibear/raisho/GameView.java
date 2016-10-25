package cs371m.tardibear.raisho;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by itoro on 10/25/16.
 */

public class GameView extends View {

    private Paint brush;
    //TODO: probably want a 2d array of map,


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        brush = new Paint();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameView, 0, 0);

//        try {
//            textLabel = a.getString(R.styleable.TView_textLabel);
//            textResult = a.getInteger(R.styleable.TView_textResult,0);
//            textColor = a.getInteger(R.styleable.TView_textColor, 0);
//            isTGrid = a.getBoolean(R.styleable.TView_isTGrid, false);
//            isTStatus = a.getBoolean(R.styleable.TView_isTStatus, false);
//            isTNext = a.getBoolean(R.styleable.TView_isTNext, false);
//        } finally {
//            a.recycle();
//        }
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // code for change background from http://stackoverflow.com/questions/28174635/android-canvas-change-background-color
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        /* code for drawing Drawables from http://stackoverflow.com/questions/5176441/drawable-image-on-a-canvas
           and http://stackoverflow.com/questions/29041027/android-getresources-getdrawable-deprecated-api-22
           and http://stackoverflow.com/questions/4552833/how-to-get-width-and-height-of-the-image */
        Drawable hex = ResourcesCompat.getDrawable(getResources(), R.drawable.dirt_03, null);
        int hexWidth = hex.getIntrinsicWidth();
        int hexHeight = hex.getIntrinsicHeight();

        //TODO: Arrange Even and Odd Rows
        for(int w = 0; w < width; w += hexWidth){
            for(int h = 0; h < height; h += hexHeight){
                hex.setBounds(w, h, w+hexWidth, h+hexHeight);
                hex.draw(canvas);
            }
        }

    }

    //TODO: Setter and Getters
}
