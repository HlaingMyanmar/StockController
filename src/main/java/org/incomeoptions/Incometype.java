package org.incomeoptions;

public class Incometype {

    private  int cat_id;
    private  String cat_name;
    private  String description;

    public Incometype() {}

    public Incometype(int cat_id, String cat_name, String description) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.description = description;
    }


    public Incometype(String cat_name, String description) {
        this.cat_name = cat_name;
        this.description = description;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
