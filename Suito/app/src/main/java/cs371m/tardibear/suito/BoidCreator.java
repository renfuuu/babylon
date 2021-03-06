package cs371m.tardibear.suito;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by kaleb on 11/30/2016.
 */

public class BoidCreator extends Activity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText sizeEditText;
    private SeekBar redSlider;
    private SeekBar greenSlider;
    private SeekBar blueSlider;
    private SeekBar separateSlider;
    private SeekBar alignSlider;
    private SeekBar cohereSlider;

    private Button createButton;
    private Button cancelButton;

    private TextView snackBarText;

    private MediaPlayer track;
    boolean playing;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {

        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.boid_creator);

        nameEditText = (EditText) findViewById(R.id.boid_name);
        sizeEditText = (EditText) findViewById(R.id.boid_size_box);

        redSlider = (SeekBar) findViewById(R.id.red_slider);
        greenSlider = (SeekBar) findViewById(R.id.green_slider);
        blueSlider = (SeekBar) findViewById(R.id.blue_slider);
        separateSlider = (SeekBar) findViewById(R.id.separate_slider);
        alignSlider = (SeekBar) findViewById(R.id.align_slider);
        cohereSlider = (SeekBar) findViewById(R.id.cohere_slider);
        createButton = (Button) findViewById(R.id.create_boid_button);

        createButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        snackBarText = (TextView) findViewById(R.id.snackbar_text);


        redSlider.setMax(256);
        greenSlider.setMax(256);
        blueSlider.setMax(256);

        separateSlider.setMax(200);
        alignSlider.setMax(200);
        cohereSlider.setMax(200);

        configureSeekBar(redSlider);
        configureSeekBar(greenSlider);
        configureSeekBar(blueSlider);
        configureSeekBar(separateSlider);
        configureSeekBar(alignSlider);
        configureSeekBar(cohereSlider);

        track = MainActivity.track;
        if(MainActivity.playing){
            track.start();
        }
    }

    private void configureSeekBar(SeekBar sb) {
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        Intent result = new Intent();

        float size = -1;
        if(!sizeEditText.getText().toString().equals("")){
            size = Float.parseFloat(sizeEditText.getText().toString());
        }

        float red = (float) redSlider.getProgress()/ (float) redSlider.getMax();
        float green =  (float) greenSlider.getProgress()/ (float) greenSlider.getMax();
        float blue = (float) blueSlider.getProgress()/ (float) blueSlider.getMax();

        float sepCoef = (float) separateSlider.getProgress()/separateSlider.getMax();
        float aliCoef = (float) alignSlider.getProgress()/alignSlider.getMax();
        float cohCoef = (float) cohereSlider.getProgress()/cohereSlider.getMax();

        Log.d("incentives: ", "" + sepCoef + "," + aliCoef + "," + cohCoef);

        String name = nameEditText.getText().toString();

        result.putExtra("name", name);
        result.putExtra("size", size);
        result.putExtra("red", red);
        result.putExtra("blue", blue);
        result.putExtra("green", green);
        result.putExtra("sep", sepCoef);
        result.putExtra("ali", aliCoef);
        result.putExtra("coh", cohCoef);

        if(name.equals(null) || name == null || size == -1 || name.equals("")){
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            Snackbar.make(snackBarText, "Enter Correct Values or Press Cancel", Snackbar.LENGTH_LONG).show();
        }
        else if(size > 39){
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            Snackbar.make(snackBarText, "Enter a size less than 40", Snackbar.LENGTH_LONG).show();
        }
        else{
            setResult(RESULT_OK, result);
            finish();
        }
    }

    @Override
    protected void onPause()
    {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        if(MainActivity.playing){
            track.pause();
        }
    }

    @Override
    protected void onResume()
    {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        if(MainActivity.playing){
            track.start();
        }

    }

}
