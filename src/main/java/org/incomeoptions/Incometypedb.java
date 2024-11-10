package org.incomeoptions;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("incomedb")

public class Incometypedb implements DataAccessObject<Incometype> {

    private NamedParameterJdbcTemplate jdbc;


    @Autowired
    public void setJdbc(DataSource jdbc) {


        this.jdbc = new NamedParameterJdbcTemplate(jdbc);

    }

    @Override
    public List<Incometype> getAllList() {


        String sql = "SELECT * FROM `income_category` WHERE 1";

        return jdbc.query(sql, (rs, rowNum) -> getIncomeTypes(rs));
    }

    @Override
    public Brand getBrandById(String id, String name) {
        return null;
    }

    @Override
    public int getDeleteById(String id) {

        String sql  = "DELETE FROM `income_category` WHERE cat_id=:cat_id";

        MapSqlParameterSource parameterSource =  new MapSqlParameterSource("cat_id", id);

        return  jdbc.update(sql, parameterSource);

    }
    public int getDeleteById(int id) {

        String sql  = "DELETE FROM `income_category` WHERE cat_id=:cat_id";

        MapSqlParameterSource parameterSource =  new MapSqlParameterSource("cat_id", id);

        return  jdbc.update(sql, parameterSource);

    }


    @Override
    public int insert(Incometype incometype) {

        String sql ="INSERT INTO `income_category`(`cat_name`, `description`) VALUES (:cat_name,:description)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(incometype);

        return jdbc.update(sql,param);
    }

    @Override
    public int update(Incometype incometype) {

        String sql = "UPDATE `income_category` SET `cat_name`=:cat_name,`description`=:description WHERE `cat_id`=:cat_id";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(incometype);

        return jdbc.update(sql,param);
    }

    @Override
    public int[] updateBatch(List<Incometype> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Incometype> t) {
        return new int[0];
    }

    private Incometype getIncomeTypes(ResultSet rs) throws SQLException {


            Incometype incometype = new Incometype();
            incometype.setCat_id(rs.getInt("cat_id"));
            incometype.setCat_name(rs.getString("cat_name"));
            incometype.setDescription(rs.getString("description"));

            return incometype;


    }
}
