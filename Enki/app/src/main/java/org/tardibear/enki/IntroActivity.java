package org.tardibear.enki;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private boolean continueMusic;
    private BackgroundMusic bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        FragmentManager fg = getSupportFragmentManager();
        FragmentTransaction fragTrans = fg.beginTransaction();

        MenuFragment menuFragment = new MenuFragment();
        fragTrans.add(R.id.main_fragment , menuFragment);
        fragTrans.commit();

        //background music
        continueMusic = true;
        bgm = new BackgroundMusic();
        bgm.start(this, bgm.MUSIC_MENU, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        continueMusic = false;
        BackgroundMusic.start(this, bgm.MUSIC_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            bgm.pause();
        }
    }

}
