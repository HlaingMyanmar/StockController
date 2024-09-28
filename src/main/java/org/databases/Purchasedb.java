package org.databases;


import org.DAO.DataAccessObject;
import org.models.Brand;
import org.models.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("purchasedb")
public class Purchasedb implements DataAccessObject<Purchase> {

    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }

    @Override
    public List<Purchase> getAllList() {
        
        return jdbc.query(" SELECT `puid`, `pudate` \n" +
                "FROM `purchase` \n" +
                "ORDER BY `pudate` DESC, CAST(SUBSTRING(`puid`, 13) AS UNSIGNED) DESC", (rs, rowNum) ->getPurchase(rs));
    }
    public List<Purchase> getAllListID() {
        return jdbc.query("SELECT * FROM `purchase` WHERE 1", (rs, rowNum) ->getPurchase(rs));
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
    public int insert(Purchase purchase) {

        String sql = "INSERT INTO `purchase`(`puid`, `pudate`) VALUES (:puid,:pudate)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(purchase);

        return jdbc.update(sql,param );
    }

    @Override
    public int update(Purchase purchase) {
        return 0;
    }

    @Override
    public int[] updateBatch(List<Purchase> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Purchase> t) {
        return new int[0];
    }


    public Purchase getPurchase(ResultSet rs) throws SQLException {

        Purchase purchase = new Purchase();

        purchase.setPuid(rs.getString("puid"));
        purchase.setPudate(rs.getDate("pudate"));

        return  purchase;

    }

}
