package org.saleoptions;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("saledb")
public class Saledb implements DataAccessObject<Sale> {


    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }



    @Override
    public List<Sale> getAllList() {
        return List.of();
    }

    @Override
    public Brand getBrandById(String id, String name) {
        return null;
    }

    @Override
    public int getDeleteById(String id) {

        String sql = "DELETE FROM `sale`  WHERE oid =' ' AND stockcode = '' ";



        return 0;
    }

    public int getDeleteById(String orderid,String stockcode) {

        String sql = "DELETE FROM `sale`  WHERE oid =:orderid AND stockcode =:stockcode";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("orderid", orderid);
        parameters.addValue("stockcode", stockcode);

        return jdbc.update(sql, parameters);


    }

    @Override
    public int insert(Sale sale) {

        String sql = "INSERT INTO `sale`(`stockcode`, `oid`, `wid`, `qty`, `price`, `discount`) VALUES (:stockcode,:oid,:wid,:qty,:price,:discount)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(sale);

        return jdbc.update(sql, param);
    }



    @Override
    public int update(Sale sale) {


        String sql = "UPDATE `sale` SET `wid`=:wid,`qty`=:qty,`price`=:price,`discount`=:discount WHERE `stockcode`=:stockcode  AND `oid` = :oid";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("wid", sale.getWid());
        parameters.addValue("qty", sale.getQty());
        parameters.addValue("price", sale.getPrice());
        parameters.addValue("discount", sale.getDiscount());
        parameters.addValue("stockcode", sale.getStockcode());
        parameters.addValue("oid", sale.getOid());

        return jdbc.update(sql, parameters);
    }

    @Override
    public int[] updateBatch(List<Sale> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Sale> t) {

        String sql = "INSERT INTO `sale`(`stockcode`, `oid`, `wid`, `qty`, `price`, `discount`) VALUES (:stockcode,:oid,:wid,:qty,:price,:discount)";

        SqlParameterSource[] param = SqlParameterSourceUtils.createBatch(t.toArray());

        return jdbc.batchUpdate(sql,param);
    }



    public  Sale getSale(ResultSet rs) throws SQLException {

        Sale sale = new Sale();


        sale.setStockcode(rs.getString("stockcode"));
        sale.setOid(rs.getString("oid"));
        sale.setWid(rs.getInt("wid"));
        sale.setQty(rs.getInt("qty"));
        sale.setPrice(rs.getInt("price"));
        sale.setDiscount(rs.getInt("discount"));



        return sale;



    }
}
