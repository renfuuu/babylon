package org.tardibear.enki;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by itoro on 11/6/16.
 */

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameListViewHolder> {
    private int[] countArray;

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

    public GameListAdapter(int[] array){
        this.countArray = array;
    }

    @Override
    public GameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_count_cell, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView titleView = (TextView) view.findViewById(R.id.title);
                Toast.makeText(view.getContext(),
                        "You clicked " + titleView.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return new GameListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameListViewHolder holder, int position) {

        holder.thumbnail.setImageResource(getDrawable(position));
        holder.title.setText(TreasureChest.Loot.values()[position].name().replace("_", " "));
        holder.count.setText(""+ countArray[position]);


        Animation animation = AnimationUtils.loadAnimation(holder.container.getContext(),
                android.R.anim.slide_in_left);
        holder.container.startAnimation(animation);

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

    public int getDrawable(int position){
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
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return countArray.length;
    }
}
