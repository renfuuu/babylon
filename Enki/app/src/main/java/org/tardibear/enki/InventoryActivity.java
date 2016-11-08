package org.tardibear.enki;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity implements GameListAdapter.Callback{

    private GameListAdapter gAdapter;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private DrawerListAdapter dAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        recyclerView = (RecyclerView) findViewById(R.id.inventory_view);
        Bundle bundle = getIntent().getExtras();
        int[] data = bundle.getIntArray("loot");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_inventory);
        listView = (ListView) findViewById(R.id.drawer_list_view);

        String[] lootNames = getResources().getStringArray(R.array.loot_names);
        String[] lootDesc = getResources().getStringArray(R.array.loot_desc);
        String[] lootOptions = getResources().getStringArray(R.array.loot_options);

        dAdapter = new DrawerListAdapter(this);
        dAdapter.setDrawerLayout(drawerLayout);
        dAdapter.setFullList(setup(lootNames, lootDesc, lootOptions));

        gAdapter = new GameListAdapter(data, dAdapter, this);
        gAdapter.setDrawerLayout(drawerLayout);
        gAdapter.setListView(listView);
        recyclerView.setAdapter(gAdapter);
    }

    @Override
    public String getTitle(int position){
        String[] titles = getResources().getStringArray(R.array.loot_names);
        return titles[position];
    }

    @Override
    public int getImage(int position){
        TreasureChest.Loot selected = TreasureChest.Loot.values()[position];
        switch (selected){
            case AK_47:
                return R.drawable.ic_inventory_ak_47;
            case AK_47_AMMO:
                return R.drawable.ic_inventory_ak_47_ammo;
            case BINOCULARS:
                return R.drawable.ic_inventory_binoculars;
            case CROWBAR:
                return R.drawable.ic_inventory_crowbar;
            case KNIFE:
                return R.drawable.ic_inventory_knife;
            case MEDKIT:
                return R.drawable.ic_inventory_medkit;
            case PISTOL:
                return R.drawable.ic_inventory_pistol;
            case PISTOL_AMMO:
                return R.drawable.ic_inventory_pistol_ammo;
            case WALKIE_TALKIE:
                return R.drawable.ic_inventory_walkie_talkie;
            default:
                return 0;
        }
    }

    public List<ArrayList<String>> setup(String[] names, String[] desc, String[] options) {
        List<ArrayList<String>> result = new ArrayList<>();
        for(int i = 0; i < names.length; i++){
            ArrayList<String> temp = new ArrayList<>();
            temp.add(names[i]);
            temp.add(desc[i]);
            temp.add("\n");
            if(i == 0 || i == 1 || i == 4 || i == 5) temp.add(options[0]);
            temp.add(options[1]);

            result.add(temp);
        }
        return result;
    }
}
