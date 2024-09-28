package org.models;

public class Supplier {

    private String sid ;
    private String suname;
    private String suphone;
    private String remarks;
    private String suaddress;


    public Supplier(String sid, String suname, String suphone, String remarks, String suaddress) {
        this.sid = sid;
        this.suname = suname;
        this.suphone = suphone;
        this.remarks = remarks;
        this.suaddress = suaddress;
    }

    public Supplier(){}

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSuname() {
        return suname;
    }

    public void setSuname(String suname) {
        this.suname = suname;
    }

    public String getSuphone() {
        return suphone;
    }

    public void setSuphone(String suphone) {
        this.suphone = suphone;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSuaddress() {
        return suaddress;
    }

    public void setSuaddress(String suaddress) {
        this.suaddress = suaddress;
    }


    @Override
    public String toString() {
        return "Supplier{" +
                "sid='" + sid + '\'' +
                ", suname='" + suname + '\'' +
                ", suphone='" + suphone + '\'' +
                ", remarks='" + remarks + '\'' +
                ", suaddress='" + suaddress + '\'' +
                '}';
    }
}
