package cs371m.tardibear.suito;

import cs371m.tardibear.suito.gfx.Obj;

/**
 * Created by itoro on 11/22/16.
 */

public class ObjModel {

    private String name;
    private Obj obj;

    public ObjModel(){

    }

    public ObjModel(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public Obj getObj(){
        return this.obj;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setObj(Obj obj){
        this.obj = obj;
    }
}
