package org.databases;



import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("brandb")
public class Branddb  implements DataAccessObject<Brand> {


    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }


    @Override
    public List<Brand> getAllList() {

        return jdbc.query("SELECT * FROM `brand` ORDER BY cast(SubString(bid,15) as UNSIGNED) DESC ", (rs, rowNum) ->getBrand(rs));


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

    public int insert(Brand brand) {

        String sql = "INSERT INTO `brand`(`bid`, `bname`) VALUES (:bid,:bname)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(brand);

        return jdbc.update(sql,param );

    }

    @Override
    public int update(Brand brand) {



        String sql = "UPDATE `brand` SET `bname` = :bname WHERE `brand`.`bid` = :bid;";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(brand);

        return jdbc.update(sql,param );

    }

    @Override
    public int[] updateBatch(List<Brand> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Brand> t) {
        return new int[0];
    }


    public Brand getBrand(ResultSet rs) throws SQLException {

    Brand brand = new Brand();

    brand.setBid(rs.getString("bid"));
    brand.setBname(rs.getString("bname"));

    return  brand;

}




}
