package org.tardibear.enki;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        FragmentManager fg = getSupportFragmentManager();
        FragmentTransaction fragTrans = fg.beginTransaction();

        MenuFragment menuFragment = new MenuFragment();
        fragTrans.add(R.id.main_fragment ,menuFragment);
        fragTrans.commit();
    }



}
