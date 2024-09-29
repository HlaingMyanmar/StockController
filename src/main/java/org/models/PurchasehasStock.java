package org.models;

public class PurchasehasStock {

    private String puid;
    private String sid;
    private String stockcode;
    private int qty;
    private int org_price;
    private  String remarks;



    public PurchasehasStock(String puid, String sid, String stockcode, int qty, int org_price, String remarks) {
        this.puid = puid;
        this.sid = sid;
        this.stockcode = stockcode;
        this.qty = qty;
        this.org_price = org_price;
        this.remarks = remarks;
    }
    public PurchasehasStock(String puid, String sid, String stockcode, int qty, int org_price) {
        this.puid = puid;
        this.sid = sid;
        this.stockcode = stockcode;
        this.qty = qty;
        this.org_price = org_price;


    }


    public PurchasehasStock(String puid, String stockcode, int qty,int org_price) {
        this.puid = puid;
        this.stockcode = stockcode;
        this.qty = qty;
        this.org_price = org_price;

    }

    public PurchasehasStock(){};




    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getOrg_price() {
        return org_price;
    }

    public void setOrg_price(int org_price) {
        this.org_price = org_price;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "PurchasehasStock{" +
                "puid='" + puid + '\'' +
                ", sid='" + sid + '\'' +
                ", stockcode='" + stockcode + '\'' +
                ", qty=" + qty +
                ", org_price=" + org_price +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
