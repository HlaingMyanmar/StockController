package org.models;

public class PurchaseStockList {

    private String stockname;
    private int qty;
    private int price;
    private int total;

    public PurchaseStockList(){}

    public PurchaseStockList(String stockname, int qty, int price, int total) {
        this.stockname = stockname;
        this.qty = qty;
        this.price = price;
        this.total = total;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
