package org.tardibear.enki;

import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by itoro on 11/6/16.
 */

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameListViewHolder> {
    private int[] countArray;
    private DrawerLayout drawerLayout;
    private ListView listView;
    protected Callback callback = null;
    private DrawerListAdapter dAdapter;


    public interface Callback{
        String getTitle(int position);
        int getImage(int position);
    }

    public class GameListViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title;
        public TextView count;
        public View container;

        public GameListViewHolder(View view) {
            super(view);
            container = view;
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
        }
    }

    public GameListAdapter(int[] array, DrawerListAdapter adapter, Callback callback){
        this.countArray = array;
        this.callback = callback;
        dAdapter = adapter;
    }

    @Override
    public GameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_count_cell, parent, false);

        return new GameListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameListViewHolder holder, final int position) {

        holder.thumbnail.setImageResource(callback.getImage(position));
        holder.title.setText(callback.getTitle(position) + ": ");
        holder.count.setText(""+ countArray[position]);


        Animation animation = AnimationUtils.loadAnimation(holder.container.getContext(),
                android.R.anim.slide_in_left);
        holder.container.startAnimation(animation);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dAdapter.setList(position);
                listView.setAdapter(dAdapter);
                drawerLayout.openDrawer(listView);
            }
        });

//        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Animation animation = AnimationUtils.loadAnimation(view.getContext(),
//                        android.R.anim.slide_out_right);
//                view.startAnimation(animation);
//                albums.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, albums.size());
//                return true; /* CONSUME this click */
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return countArray.length;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout){
        this.drawerLayout = drawerLayout;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
