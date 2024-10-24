package org.orderoptions;

import java.sql.Date;

public class OrderDataView {

    private String oid;
    private Date odate;
    private String cuname;
    private String wdesc;
    private int total;
    private String remarks;

    public OrderDataView(String oid, Date odate, String cuname, String wdesc, int total, String remarks) {
        this.oid = oid;
        this.odate = odate;
        this.cuname = cuname;
        this.wdesc = wdesc;
        this.total = total;
        this.remarks = remarks;
    }

    public OrderDataView() {}

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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



    public String getWdesc() {
        return wdesc;
    }

    public void setWdesc(String wdesc) {
        this.wdesc = wdesc;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
