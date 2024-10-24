package org.saleorderoptions;

public class SaleOrder {

    private String stockcode;
    private String stockname;
    private String wdesc;
    private int qty;
    private int price;
    private int discount;
    private int total;
    private int payid;

    public SaleOrder(){};

    public SaleOrder(String stockcode, String stockname, String wdesc, int qty, int price, int discount, int total) {
        this.stockcode = stockcode;
        this.stockname = stockname;
        this.wdesc = wdesc;
        this.qty = qty;
        this.price = price;
        this.discount = discount;
        this.total = total;
    }

    public SaleOrder(String stockcode, String stockname, String wdesc, int qty, int price, int discount, int total, int payid) {
        this.stockcode = stockcode;
        this.stockname = stockname;
        this.wdesc = wdesc;
        this.qty = qty;
        this.price = price;
        this.discount = discount;
        this.total = total;
        this.payid = payid;
    }

    public int getPayid() {
        return payid;
    }

    public void setPayid(int payid) {
        this.payid = payid;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public String getWdesc() {
        return wdesc;
    }

    public void setWdesc(String wdesc) {
        this.wdesc = wdesc;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
