package org.stockServices;

import java.sql.Date;

public class StockSetPrice {

    private String puid;
    private Date pudate;
    private int price;


    public StockSetPrice() {}

    public StockSetPrice(String puid, Date pudate, int price) {
        this.puid = puid;
        this.pudate = pudate;
        this.price = price;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public Date getPudate() {
        return pudate;
    }

    public void setPudate(Date pudate) {
        this.pudate = pudate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
