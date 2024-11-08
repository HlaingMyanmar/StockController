package org.expense_options;

import java.sql.Date;
import java.sql.Timestamp;

public class Exp_View {

    private String expense_id;
    private Date expense_date;
    private int category_id;
    private int total;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;


    public Exp_View(){}

    public Exp_View(String expense_id, Date expense_date, int category_id, int total, String description, Timestamp created_at, Timestamp updated_at) {
        this.expense_id = expense_id;
        this.expense_date = expense_date;
        this.category_id = category_id;
        this.total = total;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Exp_View(String expense_id, Date expense_date, int category_id, int total, String description) {
        this.expense_id = expense_id;
        this.expense_date = expense_date;
        this.category_id = category_id;
        this.total = total;
        this.description = description;
    }

    public String getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
    }

    public Date getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(Date expense_date) {
        this.expense_date = expense_date;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
