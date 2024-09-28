package org.models;

import java.sql.Date;

public class PurchaseList {

    private Date pudate;
    private String puid;
    private String suname;
    private int total;

    public PurchaseList(Date pudate, String puid, String suname, int total) {
        this.pudate = pudate;
        this.puid = puid;
        this.suname = suname;
        this.total = total;
    }

    public PurchaseList(){}

    public Date getPudate() {
        return pudate;
    }

    public void setPudate(Date pudate) {
        this.pudate = pudate;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getSuname() {
        return suname;
    }

    public void setSuname(String suname) {
        this.suname = suname;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
