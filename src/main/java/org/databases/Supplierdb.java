package org.databases;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.models.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("supplierdb")
public class Supplierdb implements DataAccessObject<Supplier> {

    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }

    @Override
    public List<Supplier> getAllList() {
        return jdbc.query("SELECT * FROM supplier ORDER by cast(SubString(sid,4) as UNSIGNED) DESC", (rs, rowNum) ->getSupplier(rs));

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
    public int insert(Supplier supplier) {
        String sql = "INSERT INTO supplier(sid, suname, suphone, suaddress,remarks) VALUES (:sid,:suname,:suphone,:suaddress,:remarks)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(supplier);

        return jdbc.update(sql,param );
    }

    @Override
    public int update(Supplier supplier) {

        String sql = "UPDATE supplier SET suname=:suname,suphone=:suphone,suaddress=:suaddress,remarks=:remarks WHERE sid=:sid";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(supplier);

        return jdbc.update(sql,param );
    }

    @Override
    public int[] updateBatch(List<Supplier> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Supplier> t) {
        return new int[0];
    }

    public Supplier getSupplier(ResultSet rs) throws SQLException {

        Supplier supplier = new Supplier();

        supplier.setSid(rs.getString("sid"));
        supplier.setSuname(rs.getString("suname"));
        supplier.setSuphone(rs.getString("suphone"));
        supplier.setRemarks(rs.getString("remarks"));
        supplier.setSuaddress(rs.getString("suaddress"));

        return supplier;

    }
}
