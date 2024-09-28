package org.models;

public class Category {

    private String cid;
    private String cname;
    private String cidd;

    public Category(String cid, String cname, String cidd) {
        this.cid = cid;
        this.cname = cname;
        this.cidd = cidd;
    }

    public Category(){}

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCidd() {
        return cidd;
    }

    public void setCidd(String cidd) {
        this.cidd = cidd;
    }

    @Override
    public String toString() {
        return "Category{" +
                "cid='" + cid + '\'' +
                ", cname='" + cname + '\'' +
                ", cidd='" + cidd + '\'' +
                '}';
    }
}
