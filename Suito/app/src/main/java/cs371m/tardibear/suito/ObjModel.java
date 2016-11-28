package cs371m.tardibear.suito;

import cs371m.tardibear.suito.gfx.Obj;

/**
 * Created by itoro on 11/22/16.
 */

public class ObjModel {

    private String name;
    private boolean isDefault;

    public ObjModel(String name, boolean isDefault){
        this.name = name;
        this.isDefault = isDefault;
    }

    public String getName(){
        return this.name;
    }

    public boolean getIsDefault(){
        return this.isDefault;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setisDefault(boolean isDefault){
        this.isDefault = isDefault;
    }
}
