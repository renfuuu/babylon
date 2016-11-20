package org.tardibear.enki;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import junit.framework.Assert;

/**
 * Created by itoro on 11/17/16.
 */

public class CollectablesFragment extends Fragment implements GameListAdapter.Callback{

    private GameListAdapter gAdapter;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private DrawerListAdapter dAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the root view and cache references to vital UI elements
        View v = inflater.inflate(R.layout.activity_inventory, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.inventory_view);
//        Bundle bundle = getIntent().getExtras();
//        int[] data = bundle.getIntArray("loot");
        Assert.assertNotNull(recyclerView);
        int[] data = new int[9];

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4);
        Assert.assertNotNull(mLayoutManager);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        drawerLayout = (DrawerLayout) v.findViewById(R.id.activity_inventory);
        listView = (ListView) v.findViewById(R.id.drawer_list_view);

        int width = getResources().getDisplayMetrics().widthPixels/3*2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) listView.getLayoutParams();
        params.width = width;
        listView.setLayoutParams(params);

        //TODO: change the arrays
        String[] cNames = getResources().getStringArray(R.array.loot_names);
        String[] cDesc = getResources().getStringArray(R.array.loot_desc);
        String[] cOptions = getResources().getStringArray(R.array.loot_options);

        dAdapter = new DrawerListAdapter(getContext());
        dAdapter.setDrawerLayout(drawerLayout);
        dAdapter.setFullList(InventoryActivity.setup(cNames, cDesc, cOptions));

        gAdapter = new GameListAdapter(data, dAdapter, this);
        gAdapter.setDrawerLayout(drawerLayout);
        gAdapter.setListView(listView);
        recyclerView.setAdapter(gAdapter);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public String getTitle(int position){
        String[] titles = getResources().getStringArray(R.array.loot_names);
//        return titles[position];
        return "";
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
}
