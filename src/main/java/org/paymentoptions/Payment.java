package org.paymentoptions;

import java.sql.Timestamp;

public class Payment {

    private int payid;
    private String paymethodname;
    private String payaccount;
    private int total;
    private Timestamp created_at;
    private String accountname;


    public Payment(){};


    public Payment(String paymethodname, String payaccount, String accountname,int total) {
        this.paymethodname = paymethodname;
        this.payaccount = payaccount;
        this.accountname = accountname;
        this.total = total;

    }

    public Payment(int payid, int total) {
        this.payid = payid;
        this.total = total;
    }

    public Payment(int payid, String paymethodname, String payaccount, int total, Timestamp created_at, String accountname) {
        this.payid = payid;
        this.paymethodname = paymethodname;
        this.payaccount = payaccount;
        this.total = total;
        this.created_at = created_at;
        this.accountname = accountname;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public int getPayid() {
        return payid;
    }

    public void setPayid(int payid) {
        this.payid = payid;
    }

    public String getPaymethodname() {
        return paymethodname;
    }

    public void setPaymethodname(String paymethodname) {
        this.paymethodname = paymethodname;
    }

    public String getPayaccount() {
        return payaccount;
    }

    public void setPayaccount(String payaccount) {
        this.payaccount = payaccount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
