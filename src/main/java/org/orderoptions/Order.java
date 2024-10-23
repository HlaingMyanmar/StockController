package org.orderoptions;

import java.sql.Date;

public class Order {

    private String oid;
    private Date odate;
    private String cuname;
    private String cuphone;
    private String remark;
    private int payid;
    private int total;


    Order(){}

    public Order(String oid, Date odate, String cuname, String cuphone, String remark, int payid) {
        this.oid = oid;
        this.odate = odate;
        this.cuname = cuname;
        this.cuphone = cuphone;
        this.remark = remark;
        this.payid = payid;
    }

    public Order(String oid, Date odate, String cuname, String cuphone, String remark, int payid, int total) {
        this.oid = oid;
        this.odate = odate;
        this.cuname = cuname;
        this.cuphone = cuphone;
        this.remark = remark;
        this.payid = payid;
        this.total = total;
    }

    public Order(String oid, Date odate, String cuname, String cuphone, int payid) {
        this.oid = oid;
        this.odate = odate;
        this.cuname = cuname;
        this.cuphone = cuphone;
        this.payid = payid;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getOdate() {
        return odate;
    }

    public void setOdate(Date odate) {
        this.odate = odate;
    }

    public String getCuname() {
        return cuname;
    }

    public void setCuname(String cuname) {
        this.cuname = cuname;
    }

    public String getCuphone() {
        return cuphone;
    }

    public void setCuphone(String cuphone) {
        this.cuphone = cuphone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPayid() {
        return payid;
    }

    public void setPayid(int payid) {
        this.payid = payid;
    }
}
