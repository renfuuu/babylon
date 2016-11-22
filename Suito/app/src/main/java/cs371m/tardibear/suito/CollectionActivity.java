package cs371m.tardibear.suito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cs371m.tardibear.suito.gfx.Obj;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        List<ObjModel> models = new ArrayList<>();
        models.add(new ObjModel("Bunny"));
        models.add(new ObjModel("Dragon"));
        models.add(new ObjModel("Triangle"));
        models.add(new ObjModel("Sphere"));

        for( ObjModel m : models){

            Log.d("Model Names",  m.getName());
        }


        ObjListAdapter adapter = new ObjListAdapter(this, models);

        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(adapter);
    }
}
