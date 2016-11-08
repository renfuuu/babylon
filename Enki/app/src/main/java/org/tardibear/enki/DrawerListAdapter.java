package org.tardibear.enki;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itoro on 11/8/16.
 */

public class DrawerListAdapter extends BaseAdapter {
    private List<ArrayList<String>> drawerLists;
    private List<String> drawerList;
    private LayoutInflater mInflater;
    private DrawerLayout drawerLayout;


    public DrawerListAdapter(Context mContext){
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        drawerLists = null;
        drawerList = null;
    }

    public void setFullList(List<ArrayList<String>> list){
        drawerLists = list;
    }

    public void setList(int position){
        drawerList = drawerLists.get(position);
    }

    @Override
    public int getCount() {
        return drawerList.size();
    }

    @Override
    public String getItem(int position) {
        return drawerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    private View findViewBindView(int position, final View theView) {
        // We retrieve the text from the array
        String text = getItem(position);
        TextView theTextView = (TextView) theView.findViewById(R.id.drawer_text);
        if(position == 0){
            theTextView.setTypeface(null, Typeface.ITALIC);
        }
        if(position == 1){
            theTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
        theTextView.setText(text);
        theTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        return theView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_item, parent, false);
        }
        convertView = findViewBindView(position, convertView);
        return convertView;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }
}
