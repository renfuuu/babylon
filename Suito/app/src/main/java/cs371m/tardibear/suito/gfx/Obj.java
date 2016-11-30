package cs371m.tardibear.suito.gfx;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by itoro on 11/20/16.
 */

public class Obj {

    public short[] indices;
    private ArrayList<Short> indicesList;
    public float[] vertices;
    private ArrayList<Float> verticesList;

    private String TAG = "Obj";


    public Obj(String objFile){
        load(objFile);
    }

    public Obj(Context context, int raw){
        indicesList = new ArrayList<>();
        verticesList = new ArrayList<>();
        load(context, raw);
    }

    private void load(String objFile){
        Scanner scanner = new Scanner(objFile);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String flag = lineScanner.next();
            if(flag.equals("v")){

                //TODO: sphere doesn't show up
//                while(lineScanner.hasNext()){
//                    String vertex = lineScanner.next();
//                    verticesList.add(Float.parseFloat(vertex));
//                }
//                vertices = new float[verticesList.size()];
//                for(int i = 0; i < vertices.length; i++){
//                    vertices[i] = verticesList.get(i).floatValue();
//                }


//              TODO: null exception when sphere
                while(lineScanner.hasNextFloat()){
                    float vertex = lineScanner.nextFloat();
                    verticesList.add(vertex);
                }


            }
            else if(flag.equals("f")){
                while(lineScanner.hasNextShort()){
                    short index = lineScanner.nextShort();
                    indicesList.add(index);
                }


            }else if(flag.equals("vt")) {
                // vertex uv
            }else if(flag.equals("vn")) {
                //vertex normal/color
            }else throw new IllegalStateException();
        }


        Log.d(TAG, "num of vertices : " + verticesList.size());
        Log.d(TAG, "num of indices : " + indicesList.size());

        //TODO: check for uv, color later




        vertices = padPureVertexData();
        indices = new short[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
            indices[i] -= 1;
        }
    }

    private void load(Context context, int rawId){
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(rawId)));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");

            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Log.d("ObjLoad", sb.toString());
            load(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float[] padPureVertexData(){
        float[] padding = new float[verticesList.size()*3];
        int padindex = 0;
        for(int i = 0; i < verticesList.size(); i++){

            if(i % 3 == 0 && i >= 3){
                for(int c = 0; c < 4; c++){
                    padding[padindex+c] = 1.0f;
                }
                for(int uv = 0; uv < 2; uv++){
                    padding[padindex+uv+4] = 1.0f;
                }
                padindex += 6;
            }
            padding[padindex] = verticesList.get(i);
            padindex += 1;
        }
        return padding;
    }

}
