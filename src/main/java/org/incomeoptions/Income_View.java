package org.incomeoptions;

import java.sql.Date;
import java.sql.Timestamp;

public class Income_View {

    private String income_id;
    private Date income_date;
    private int income_type;
    private String income_name;
    private int amount;
    private int payid ;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;

    private String paymentmethod;


    public Income_View() {}


    public Income_View(String income_id, Date income_date, int income_type, int amount, int payid, String description) {
        this.income_id = income_id;
        this.income_date = income_date;
        this.income_type = income_type;
        this.amount = amount;
        this.payid = payid;
        this.description = description;
    }

    public Income_View(String income_id, int amount, int payid, Timestamp updated_at) {
        this.income_id = income_id;
        this.amount = amount;
        this.payid = payid;
        this.updated_at = updated_at;
    }

    public Income_View(String income_id, Date income_date, String description, Timestamp updated_at) {
        this.income_id = income_id;
        this.income_date = income_date;
        this.description = description;
        this.updated_at = updated_at;
    }

    public Income_View(String income_id, int payid) {
        this.income_id = income_id;
        this.payid = payid;
    }

    public Income_View(String income_id, Date income_date, int amount, int payid, String description, Timestamp updated_at, Timestamp created_at) {
        this.income_id = income_id;
        this.income_date = income_date;
        this.amount = amount;
        this.payid = payid;
        this.description = description;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public Income_View(String income_id, Date income_date, int income_type, int amount, int payid, String description, Timestamp created_at, Timestamp updated_at) {
        this.income_id = income_id;
        this.income_date = income_date;
        this.income_type = income_type;
        this.amount = amount;
        this.payid = payid;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Income_View(String income_id, Date income_date, int income_type, int amount, String paymentmethod, Timestamp updated_at, Timestamp created_at, String description) {
        this.income_id = income_id;
        this.income_date = income_date;
        this.income_type = income_type;
        this.amount = amount;
        this.paymentmethod = paymentmethod;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.description = description;
    }



    public String getPaymentmethod() {
        return paymentmethod;
    }

    public String getIncome_name() {
        return income_name;
    }

    public void setIncome_name(String income_name) {
        this.income_name = income_name;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getIncome_id() {
        return income_id;
    }

    public void setIncome_id(String income_id) {
        this.income_id = income_id;
    }

    public Date getIncome_date() {
        return income_date;
    }

    public void setIncome_date(Date income_date) {
        this.income_date = income_date;
    }

    public int getIncome_type() {
        return income_type;
    }

    public void setIncome_type(int income_type) {
        this.income_type = income_type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPayid() {
        return payid;
    }

    public void setPayid(int payid) {
        this.payid = payid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
