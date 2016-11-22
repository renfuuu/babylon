package cs371m.tardibear.suito;

import android.content.Context;
import android.content.Intent;
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

import cs371m.tardibear.suito.gfx.Obj;

/**
 * Created by itoro on 11/22/16.
 */

public class ObjListAdapter extends ArrayAdapter<ObjModel> {

    private List<ObjModel> objList;
    private LayoutInflater inflater;

    static class ViewHolder{

        public TextView textView;

        public ViewHolder(TextView _tv) {
            textView = _tv;
        }
    }

    public ObjListAdapter(Context context, List<ObjModel> list){
        super(context, R.layout.object_list_item, list);
        objList = list;
        inflater = LayoutInflater.from(getContext());
    }


    public View bindView(View convertView, final int position, ViewGroup parent){
        ViewHolder vh;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.object_list_item, parent, false);
            vh = new ViewHolder((TextView)convertView.findViewById(R.id.obj_title));
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }


        vh.textView.setText(objList.get(position).getName());
        vh.textView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO: change instead of on the textView but on the entire list
                Intent intent = new Intent(getContext(), MainActivity.class);
                Bundle b = new Bundle();
                b.putString("OBJ_NAME", objList.get(position).getName());
                intent.putExtras(b);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return bindView(view, i, viewGroup);
    }
}
