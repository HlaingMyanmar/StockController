package org.expense_options;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("exp_typesdb")

public class Exp_Typesdb implements DataAccessObject<Exp_Types> {

    private NamedParameterJdbcTemplate jdbc;


    @Autowired
    public void setJdbc(DataSource jdbc) {


        this.jdbc = new NamedParameterJdbcTemplate(jdbc);

    }

    @Override
    public List<Exp_Types> getAllList() {

        String sql ="SELECT `category_id`, `category_name`, `description` FROM `exp_category` ORDER by category_name";

        return jdbc.query(sql, (rs, rowNum) ->getExpenseType(rs));
    }

    @Override
    public Brand getBrandById(String id, String name) {
        return null;
    }

    @Override
    public int getDeleteById(String id) {

        String sql ="DELETE FROM `exp_category` WHERE `category_id` =:id";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);


        return jdbc.update(sql, parameters);
    }

    public int getDeleteById(int id){


        String sql ="DELETE FROM `exp_category` WHERE `category_id` =:id";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);


        return jdbc.update(sql, parameters);

    }

    @Override
    public int insert(Exp_Types expTypes) {

        String sql = "INSERT INTO `exp_category`(`category_name`, `description`) VALUES (:category_name,:description)";
        BeanPropertySqlParameterSource parma = new BeanPropertySqlParameterSource(expTypes);
        return jdbc.update(sql,parma);
    }

    @Override
    public int update(Exp_Types expTypes) {



        String sql ="UPDATE `exp_category` SET `category_name`=:category_name,`description`=:description WHERE `category_id`=:category_id";

        return jdbc.update(sql,new BeanPropertySqlParameterSource(expTypes));
    }

    @Override
    public int[] updateBatch(List<Exp_Types> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Exp_Types> t) {
        return new int[0];
    }

    private Exp_Types getExpenseType(ResultSet rs) throws SQLException {


        Exp_Types expTypes = new Exp_Types();

        expTypes.setCategory_id(rs.getInt("category_id"));
        expTypes.setCategory_name(rs.getString("category_name"));
        expTypes.setDescription(rs.getString("description"));

        return expTypes;


    }
}
