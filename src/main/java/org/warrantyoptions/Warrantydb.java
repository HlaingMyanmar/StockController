package org.warrantyoptions;

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

@Component("warrantydb")
public class Warrantydb implements DataAccessObject<Warranty> {



    private NamedParameterJdbcTemplate jdbc;




    @Autowired
    public void setJdbc(DataSource jdbc) {


        this.jdbc = new NamedParameterJdbcTemplate(jdbc);



    }

    @Override
    public List<Warranty> getAllList() {

        String  sql ="SELECT * FROM `warranty` WHERE 1";

        return jdbc.query(sql, (rs, rowNum) -> getWarranty(rs));
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
    public int insert(Warranty warranty) {

        String sql = "INSERT INTO `warranty`( `wdesc`) VALUES (:wdesc)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(warranty);

        return jdbc.update(sql,param);
    }

    @Override
    public int update(Warranty warranty) {

        String sql = "UPDATE `warranty` SET `wdesc`=:wdesc WHERE  `wid`=:wid";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(warranty);

        return jdbc.update(sql,param);
    }

    @Override
    public int[] updateBatch(List<Warranty> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Warranty> t) {
        return new int[0];
    }


    public Warranty getWarranty(ResultSet rs) throws SQLException {

        Warranty warranty = new Warranty();
        warranty.setWid(rs.getInt("wid"));
        warranty.setWdesc(rs.getString("wdesc"));

        return warranty;




    }
}
