package org.tardibear.enki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    private GameListAdapter gAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        recyclerView = (RecyclerView) findViewById(R.id.inventory_view);
        Bundle bundle = getIntent().getExtras();
        int[] data = bundle.getIntArray("loot");
        gAdapter = new GameListAdapter(data);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gAdapter);
    }
}
