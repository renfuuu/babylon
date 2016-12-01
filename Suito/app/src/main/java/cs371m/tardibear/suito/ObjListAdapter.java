package cs371m.tardibear.suito;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import cs371m.tardibear.suito.boids.Boid;
import cs371m.tardibear.suito.gfx.Obj;

/**
 * Created by itoro on 11/22/16.
 */

public class ObjListAdapter extends ArrayAdapter<Boid> {

    private List<Boid> objList;
    private LayoutInflater inflater;

    static class ViewHolder{

        public TextView textView;
        public ImageView imageView;

        public ViewHolder(TextView _tv, ImageView _iv) {
            textView = _tv;
            imageView = _iv;
        }
    }

    public ObjListAdapter(Context context, List<Boid> list){
        super(context, R.layout.object_list_item, list);
        objList = list;
        inflater = LayoutInflater.from(getContext());
    }


    public View bindView(View convertView, final int position, ViewGroup parent){
        ViewHolder vh;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.object_list_item, parent, false);
            vh = new ViewHolder((TextView)convertView.findViewById(R.id.obj_title), (ImageView) convertView.findViewById(R.id.obj_pic));
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }


        vh.textView.setText(objList.get(position).getName());
        vh.imageView.setBackgroundColor(Color.rgb(Math.round(objList.get(position).getColor()[0] * 255), Math.round(objList.get(position).getColor()[1] * 255), Math.round(objList.get(position).getColor()[2] * 255)));


        return convertView;
    }

    public void setObjList(List<Boid> boids){
        this.objList = boids;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return bindView(view, i, viewGroup);
    }
}
