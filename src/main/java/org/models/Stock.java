package org.models;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Stock {

    private String stockcode;
    private String stockname;
    private String bid;
    private String ccode;
    private int qty;
    private int price;
    private int total;

    public Stock(String stockcode, String stockname, String bid, String ccode, int qty, int price, int total) {
        this.stockcode = stockcode;
        this.stockname = stockname;
        this.bid = bid;
        this.ccode = ccode;
        this.qty = qty;
        this.price = price;
        this.total = total;
    }
    public Stock(String stockcode){
        this.stockcode = stockcode;
    }

    public Stock(String stockcode, int qty,int price){

        this.stockcode = stockcode;
        this.qty = qty;
        this.price = price;

    }

    public Stock(String stockcode, String stockname, String bid, String ccode, int qty, int price) {
        this.stockcode = stockcode;
        this.stockname = stockname;
        this.bid = bid;
        this.ccode = ccode;
        this.qty = qty;
        this.price = price;

    }




    public Stock(){

    }

    public Stock(AtomicReference<String> stockcode,int qty){


        this.stockcode = String.valueOf(stockcode);
        this.qty = qty;

    }

    public Stock(AtomicReference<String> stockcode,int  price,int total){


        this.stockcode = String.valueOf(stockcode);
        this.price = price;
        this.total = total;

    }


    public Stock(String stockcode, int price) {
        this.stockcode = stockcode;
        this.price = price;
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

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
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
