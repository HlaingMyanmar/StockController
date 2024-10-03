package org.warrantyoptions;

public class Warranty {

    private int wid;
    private String wdesc;


    Warranty(){};

    public Warranty(int wid, String wdesc) {
        this.wid = wid;
        this.wdesc = wdesc;
    }


    public Warranty(String wdesc) {
        this.wdesc = wdesc;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getWdesc() {
        return wdesc;
    }

    public void setWdesc(String wdesc) {
        this.wdesc = wdesc;
    }
}
