package org.tardibear.enki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.tardibear.enki.mkii.VAO;

/**
 * Created by kaleb on 3/10/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(this, IntroActivity.class);
//        startActivity(intent);


        Intent intent = new Intent(this, VAO.class);
        startActivity(intent);

        finish();
    }
}
