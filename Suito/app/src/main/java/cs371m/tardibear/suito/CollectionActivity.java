package cs371m.tardibear.suito;

import android.support.design.widget.Snackbar;
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
        models.add(new ObjModel("Bunny", true));
        models.add(new ObjModel("Dragon", true));
        models.add(new ObjModel("Triangle", true));
        models.add(new ObjModel("Sphere", true));

        ObjListAdapter adapter = new ObjListAdapter(this, models);

        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(adapter);

        Snackbar.make(lv, "Select a 3D model", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
