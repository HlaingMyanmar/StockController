package org.models;

public class Brand {

    private String bid;
    private String bname;


    public Brand(String bid, String bname) {
        this.bid = bid;
        this.bname = bname;
    }

    public Brand(){};

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "bid='" + bid + '\'' +
                ", bname='" + bname + '\'' +
                '}';
    }
}
