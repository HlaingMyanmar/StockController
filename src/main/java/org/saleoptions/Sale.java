package org.saleoptions;

public class Sale {

    private String stockcode;
    private String oid;
    private int wid;
    private int qty;
    private int price;
    private int discount;


    public Sale(){}


    public Sale(String stockcode, String oid, int wid, int qty, int price, int discount) {
        this.stockcode = stockcode;
        this.oid = oid;
        this.wid = wid;
        this.qty = qty;
        this.price = price;
        this.discount = discount;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
