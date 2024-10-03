package org.paymentoptions;


import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("paymentdb")
public class Paymentdb implements DataAccessObject<Payment> {

    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }


    @Override
    public List<Payment> getAllList() {

        String sql = "SELECT * FROM `payment` order by payid asc";

        return jdbc.query(sql , (rs, rowNum) ->getPayment(rs));

    }

    @Override
    public Brand getBrandById(String id, String name) {



        return null;
    }

    @Override
    public int getDeleteById(String id) {
        return 0;
    }

    @Override
    public int insert(Payment payment) {



        String sql = "INSERT INTO `payment`( `paymethodname`, `payaccount`,`accountname`,`total`, `created_at`) VALUES (:paymethodname,:payaccount,:accountname,:total,:created_at)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(payment);

        return jdbc.update(sql,param);
    }

    @Override
    public int update(Payment payment) {



        String sql = "UPDATE `payment` SET `paymethodname`=:paymethodname,`payaccount`=:payaccount ,`accountname`=:accountname WHERE `payid` = :payid";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(payment);

        return jdbc.update(sql,param);
    }

    @Override
    public int[] updateBatch(List<Payment> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Payment> t) {
        return new int[0];
    }
    public Payment getPayment(ResultSet rs) throws SQLException {

       Payment payment = new Payment();

        payment.setPayid(rs.getInt("payid"));
        payment.setPaymethodname(rs.getString("paymethodname"));
        payment.setPayaccount(rs.getString("payaccount"));
        payment.setTotal(rs.getInt("total"));
        payment.setCreated_at(rs.getTimestamp("created_at"));
        payment.setAccountname(rs.getString("accountname"));

        return  payment;

    }

}
