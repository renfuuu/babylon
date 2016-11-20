package org.tardibear.enki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by itoro on 10/29/16.
 */
public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the root view and cache references to vital UI elements
        View v = inflater.inflate(R.layout.game_menu, container, false);
        Button playButton = (Button) v.findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainGame = new Intent(getActivity(), RenderActivity.class);
                startActivity(mainGame);
            }
        });

        Button collectablesButton = (Button) v.findViewById(R.id.button_collectables);
        collectablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fg = getFragmentManager();
                FragmentTransaction fragTrans = fg.beginTransaction();

                CollectablesFragment collectablesFragment = new CollectablesFragment();
                fragTrans.add(R.id.main_fragment, collectablesFragment);
                fragTrans.addToBackStack(null);
                fragTrans.commit();
            }
        });



        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
