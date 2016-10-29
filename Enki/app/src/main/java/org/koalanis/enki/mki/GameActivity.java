package org.koalanis.enki.mki;

import android.app.Activity;
import android.os.Bundle;

import org.koalanis.enki.R;

/**
 * Created by kaleb on 25/10/16.
 */

public class GameActivity extends Activity {
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);

        gameView = (GameView)findViewById(R.id.render_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

}

